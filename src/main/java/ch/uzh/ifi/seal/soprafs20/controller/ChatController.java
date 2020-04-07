package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for actions regarding the player and spectator chats.
 */
@RestController
public class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PutMapping("/games/{gameId}/chats/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendPlayerMessage(@RequestBody ChatLog chatLog, @PathVariable long gameId, @RequestHeader (value = "Authorization") String token){return chatService.newMessage("players", gameId, chatLog);}

    @GetMapping("/games/{gameId}/chats/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getPlayerMessageHistory(@PathVariable long gameId, @RequestHeader (value = "Authorization") String token){ return chatService.getHistory("players", gameId);}

    @PutMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendSpectatorMessage(@RequestBody ChatLog chatLog, @PathVariable long gameId, @RequestHeader (value = "Authorization") String token){return chatService.newMessage("spectators", gameId, chatLog);}

    @GetMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getSpectatorMessageHistory(@PathVariable long gameId, @RequestHeader (value = "Authorization") String token){return chatService.getHistory("spectators", gameId);}

}
