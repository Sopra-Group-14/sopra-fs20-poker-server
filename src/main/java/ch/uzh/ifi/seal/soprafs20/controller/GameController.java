package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ActionDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.JoinGameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Status;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * REST controller responsible for everything that happens in the game (gameplay-related).
 */
@RestController
public class GameController {

    private final GameService gameService;
    private final Logger log = LoggerFactory.getLogger(GameController.class);
    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getAllGames(){return gameService.getAllGames();}

    /*
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game getGame(){return gameService.getGame();}
*/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game createGame(@RequestBody GamePostDTO gamePostDTO){
        Game postGame = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        String gameName = postGame.getGameName();
        long hostId = postGame.getGameHostID();
        String potType = postGame.getPotType();

        Game game = gameService.createGame(gameName, hostId, potType);
        gameService.addHost(hostId, game);

        return game;
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog joinGame(@RequestBody JoinGameDTO joinGameDTO, @PathVariable long gameId){

            gameService.addJoiningPlayer(joinGameDTO.getUserId(), gameId);

      /*  if(joinGameDTO.getUserMode().equals("player")){
            gameService.addJoiningPlayer(joinGameDTO.getUserId(), gameId);
        }else if(joinGameDTO.getUserMode().equals("spectator")){
            gameService.addSpectator(gameId);
        }*/

        return gameService.getGameLog(gameId);

    }

    @PutMapping("/games/{gameId}/spectator")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog joinGame( @PathVariable long gameId){

            gameService.addSpectator(gameId);

            return gameService.getGameLog(gameId);

        }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog getGameLog(@PathVariable long gameId){
        return gameService.getGameLog(gameId);
    }

    @PutMapping("games/{gameId}/gameStart")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog gameStart(@PathVariable long gameId){

        Game game = gameService.findGameById(gameId);
        game.startGame();
        return game.getGameLog();

    }

    @PutMapping("games/{gameId}/roundStart")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog roundStart(@PathVariable long gameId){

        Game game = gameService.findGameById(gameId);

        return gameService.roundStart(game);

    }

    @GetMapping("/games/{gameId}/dealers/cards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Card> getTableCards(@RequestHeader (value = "Authorization") String token){

        return gameService.getTableCards(token);
    }

    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getPlayers(@PathVariable long gameId, @RequestHeader (value = "Authorization") String token) {
        if (gameService.checkAuthorizationGet(token, gameId)) {
            List<Player> players = gameService.getPlayers(gameId);
            List<String> playerNames = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                playerNames.add(players.get(i).getPlayerName());
            }
            return playerNames;
        }
        else {
            throw new TransactionSystemException("Error: Authorization token does not match with Player tokens");
        }
    }

    @PutMapping("/games/{gameId}/players/{playerId}/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog leave(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader (value = "Authorization") String token){
        /*if (gameService.checkAuthorizationGet(token, gameId) == false) {
            throw new TransactionSystemException("Authorization check error");
        }*/

        log.info("before gameService");
        gameService.removePlayer(gameId, playerId);
        /*if (playerId == gameService.getGame(gameId).getGameLog().getNextPlayerId()){
            gameService.executeAction(Action.FOLD, 0, gameId, playerId);
        }
        */
        return gameService.getGame(gameId).getGameLog();
    }

    /*@PutMapping("/games/{gameId}/players/{playerId}/actions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog takeAction(@RequestBody Action action, @PathVariable long gameId, @PathVariable long playerId, @RequestHeader (value = "Authorization") String token){return null;}*/

    //Overloaded method for calls that use an "amount"
    @PutMapping("/games/{gameId}/players/{playerId}/actions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog takeAction(@RequestBody ActionDTO actionDTO, @PathVariable long gameId, @PathVariable long playerId, @RequestHeader (value = "Authorization") String token){
            if (!gameService.checkAuthorizationPut(gameId, playerId, token)) {
                throw new TransactionSystemException("error");
            }
            Game game = gameService.getGame(gameId);
            game.playGame(actionDTO.getAction(), playerId);
            return gameService.executeAction(actionDTO.getAction(), actionDTO.getAmount(), gameId, playerId);
    }

    @PutMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void togglePlayerReadyStatus(@PathVariable long gameId, @PathVariable long playerId){gameService.togglePlayerReadyStatus(gameId, playerId);}

    @GetMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getPlayerHandCardsByPlayerId(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader(value = "Authorization") String token){

        return gameService.findGameById(gameId).getPlayerById(playerId);

    }


    @PutMapping("/games/{gameId}/spectators/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @GetMapping("/games/{gameId}/currentPlayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable long gameId){return null;}

}
