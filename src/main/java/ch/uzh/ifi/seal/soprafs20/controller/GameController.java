package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for everything that happens in the game (gameplay-related).
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getAllGames(){return gameService.getAllGames();}

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game createGame(@RequestBody String gameName, @RequestBody long HostId, @RequestBody String potType){return GameService.createGame(gameName, HostId, potType);}

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game joinGame(@RequestBody UserPostDTO userPostDTO, @RequestBody UserStatus userStatus, @RequestHeader (value = "Authorization") String token, @PathVariable long gameId){return null;}

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog getGameLog(@PathVariable long gameId){return null;}

    @GetMapping("/games/{gameId}/table")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Card> getTableCards(@PathVariable long gameId){return gameService.getTableCards(gameId);}

    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable long gameId){return null;}

    @PutMapping("/games/{gameId}/players/{playerId}/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leave(@PathVariable long gameId, @PathVariable long playerId){gameService.removePlayer(gameId, playerId);}

    /*@PutMapping("/games/{gameId}/players/{playerId}/actions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog takeAction(@RequestBody Action action, @PathVariable long gameId, @PathVariable long playerId, @RequestHeader (value = "Authorization") String token){return null;}*/

    //Overloaded method for calls that use an "amount"
    @PutMapping("/games/{gameId}/players/{playerId}/actions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog takeAction(@RequestBody Action action, @RequestBody int amount, @PathVariable long gameId, @PathVariable long playerId, @RequestHeader String token){
        gameService.executeAction(action, amount, gameId, playerId, token);
        return null;
    }

    @PutMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makePlayer(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @PutMapping("/games/{gameId}/spectators/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @GetMapping("/games/{gameId}/currentPlayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable long gameId){return null;}

    @PutMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void toggleReadyStatus(@PathVariable long gameId, @PathVariable long playerId){
        gameService.toggleReadyStatus(gameId, playerId);
    }

}
