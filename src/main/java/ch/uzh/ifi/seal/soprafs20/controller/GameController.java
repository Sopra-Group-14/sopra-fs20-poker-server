package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
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
    public GameLog fold(@PathVariable int gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/raise/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog raise(@PathVariable int gameid, @PathVariable long playerid, @RequestBody int amount){return null;}

    @PutMapping("/games/{gameid}/call/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog call(@PathVariable int gameid, @PathVariable long playerid){return null;}

    @GetMapping("/games/{gameid}/call/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int callAmount(@PathVariable int gameid, @PathVariable long playerid){return -1;}

    @PutMapping("/games/{gameid}/check/{playerid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLog check(@PathVariable int gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/players/{playerid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makePlayer(@PathVariable int gameid, @PathVariable long playerid){return null;}

    @PutMapping("/games/{gameid}/spectators/{playerid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameLog makeSpectator(@PathVariable int gameid, @PathVariable long playerid){return null;}

    @GetMapping("/games/{gameid}/end/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameSummary endGame(@PathVariable int gameid){return null;}

    @GetMapping("/games/{gameid}/currentplayer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player currentPlayer(@PathVariable int gameid){return null;}

}
