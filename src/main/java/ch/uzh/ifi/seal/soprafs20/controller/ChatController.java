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

    @PutMapping("/chats/{gameid}/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendPlayerMessage(@RequestBody long playerid, @RequestBody String message, @PathVariable long gameid){return null;}

    @GetMapping("/chats/{gameid}/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<String> getPlayerMessageHistory(@PathVariable long gameid){return null;}

    @PutMapping("/chats/{gameid}/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendSpectatorMessage(@RequestBody long spectatorid, @RequestBody String message, @PathVariable long gameid){return null;}

    @GetMapping("/chats/{gameid}/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<String> getSpectatorMessageHistory(@PathVariable long gameid){return null;}

}
