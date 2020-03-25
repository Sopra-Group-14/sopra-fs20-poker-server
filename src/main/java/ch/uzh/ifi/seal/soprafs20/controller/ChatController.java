package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for actions regarding the player and spectator chats.
 */
@RestController
public class ChatController {

    @PutMapping("/games/{gameId}/chats/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendPlayerMessage(@RequestBody long playerId, @RequestBody String message, @PathVariable long gameId){return null;}

    @GetMapping("/games/{gameId}/chats/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<String> getPlayerMessageHistory(@PathVariable long gameId){return null;}

    @PutMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendSpectatorMessage(@RequestBody long spectatorId, @RequestBody String message, @PathVariable long gameId){return null;}

    @GetMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<String> getSpectatorMessageHistory(@PathVariable long gameId){return null;}

}
