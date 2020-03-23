package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This REST controller takes care of calls for actions outside of games; miscellaneous things.
 */
@RestController
public class OutOfGameController {

    @GetMapping("/spectators/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getGameListWhenNotLoggedIn(){return null;}

    @GetMapping("/games/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<Game> joinGame(){return null;}

    @PostMapping("/games/host")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game hostGame(@RequestBody UserPostDTO userPostDTO){return null;}

}
