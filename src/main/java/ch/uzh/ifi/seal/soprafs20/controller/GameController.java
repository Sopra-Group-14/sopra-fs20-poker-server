package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for everything that happens in the game (gameplay-related).
 */
@RestController
public class GameController {

    @PutMapping("/games/{gameId}/fold/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog fold(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @PutMapping("/games/{gameId}/raise/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog raise(@PathVariable long gameId, @PathVariable long playerId, @RequestBody int amount){return null;}

    @PutMapping("/games/{gameId}/call/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog call(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @GetMapping("/games/{gameId}/call/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int callAmount(@PathVariable long gameId, @PathVariable long playerId){return -1;}

    @PutMapping("/games/{gameId}/check/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog check(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @PutMapping("/games/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makePlayer(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @PutMapping("/games/{gameId}/spectators/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable long gameId, @PathVariable long playerId){return null;}

    @GetMapping("/games/{gameId}/end/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameSummary endGame(@PathVariable long gameId){return null;}

    @GetMapping("/games/{gameId}/currentPlayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable long gameId){return null;}

}
