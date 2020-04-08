package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.OutOfGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This REST controller takes care of calls for actions outside of games; miscellaneous things.
 */
@RestController
public class OutOfGameController {

    private final OutOfGameService outOfGameService;

    OutOfGameController(OutOfGameService outOfGameService){
        this.outOfGameService = outOfGameService;
    }

    @GetMapping("/spectators/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getAllHostedGames(){return null;}

    @GetMapping("/games/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<Game> joinGame(){return null;}

    @PostMapping("/games/host")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game hostGame(@RequestBody UserPostDTO userPostDTO){return null;}

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> activeGames(){
        List<Game> allGames = outOfGameService.getAllHostedGames();

        return null;
    }

}
