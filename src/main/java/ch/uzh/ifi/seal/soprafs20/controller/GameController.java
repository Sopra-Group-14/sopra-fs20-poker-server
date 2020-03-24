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

    @PutMapping("/games/{gameid}/fold/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog fold(@PathVariable long gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/raise/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog raise(@PathVariable long gameid, @PathVariable long playerid, @RequestBody int amount){return null;}

    @PutMapping("/games/{gameid}/call/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog call(@PathVariable long gameid, @PathVariable long playerid){return null;}

    @GetMapping("/games/{gameid}/call/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int callAmount(@PathVariable long gameid, @PathVariable long playerid){return -1;}

    @PutMapping("/games/{gameid}/check/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog check(@PathVariable long gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/players/{playerid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makePlayer(@PathVariable long gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/spectators/{playerid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable long gameid, @PathVariable long playerid){return null;}

    @GetMapping("/games/{gameid}/end/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameSummary endGame(@PathVariable long gameid){return null;}

    @GetMapping("/games/{gameid}/currentplayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable long gameid){return null;}

}
