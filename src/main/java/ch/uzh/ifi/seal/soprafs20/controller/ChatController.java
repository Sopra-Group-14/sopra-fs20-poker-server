package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.chat.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ChatPutDTO;
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
    public ChatLog sendPlayerMessage(@RequestBody ChatPutDTO chatPutDTO,
                                     @PathVariable long gameId,
                                     @RequestHeader (value = "Authorization") String token){
        chatService.newMessage("players", gameId, chatPutDTO.getChatLog());
        chatService.newMessage("specators", gameId, chatPutDTO.getChatLog());

        List<ChatLog> playerChat = chatService.getHistory("players", gameId);

        return chatService.getLatestMessage(playerChat);

    }

    @GetMapping("/games/{gameId}/chats/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatLog> getPlayerMessageHistory(@PathVariable long gameId,
                                                 @RequestHeader (value = "Authorization") String token){
        return chatService.getHistory("players", gameId);
    }

    @PutMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatLog sendSpectatorMessage(@RequestBody ChatPutDTO chatPutDTO,
                                        @PathVariable long gameId,
                                        @RequestHeader (value = "Authorization") String token){
        chatService.newMessage("spectators", gameId, chatPutDTO.getChatLog());

        List<ChatLog> spectatorChat = chatService.getHistory("spectators", gameId);

        return chatService.getLatestMessage(spectatorChat);
    }

    @GetMapping("/games/{gameId}/chats/spectators")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatLog> getSpectatorMessageHistory(@PathVariable long gameId,
                                                    @RequestHeader (value = "Authorization") String token){
        return chatService.getHistory("spectators", gameId);
    }

}
