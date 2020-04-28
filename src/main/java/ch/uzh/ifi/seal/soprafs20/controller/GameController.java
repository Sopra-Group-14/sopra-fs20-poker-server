package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ActionDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public Game joinGame(@RequestBody UserPostDTO userPostDTO, @RequestBody UserStatus userStatus, @RequestHeader (value = "Authorization") String token, @PathVariable long gameId){
        return null;
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog getGameLog(@PathVariable long gameId){
        return gameService.getGameLog(gameId);
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
        if (gameService.checkAuthorizationGet(token, gameId) == false) {
            throw new TransactionSystemException("error");
        }
        List<Player> players = gameService.getPlayers(gameId);
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            playerNames.add(players.get(i).getPlayerName());
        }
        return playerNames;
    }

    @PutMapping("/games/{gameId}/players/{playerId}/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leave(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader (value = "Authorization") String token){
        if (gameService.checkAuthorizationGet(token, gameId) == false) {
            throw new TransactionSystemException("error");
        }
        gameService.removePlayer(gameId, playerId);
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
            return gameService.executeAction(actionDTO.getAction(), actionDTO.getAmount(), gameId, playerId, token);
    }

    @PutMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void togglePlayerReadyStatus(@PathVariable long gameId, @PathVariable long playerId){gameService.togglePlayerReadyStatus(gameId, playerId);}


    @PutMapping("/games/{gameId}/spectators/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @GetMapping("/games/{gameId}/currentPlayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable long gameId){return null;}

}
