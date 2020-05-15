package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Takes care of actions regarding the chat.
 */
@Service
@Transactional
public class ChatService {

    private final GameService gameService;

    public ChatService(GameService gameService) {
        this.gameService = gameService;
    }

    public List<ChatLog> getHistory(String chatMode, long gameId){

        Game game = gameService.getGame(gameId);

        return game.getMessages(chatMode);
    }

    /*public void newMessage(Chat chat, String message){
        chat.newMessage(message);
    }*/
    public void newMessage(String chatMode, long gameId, ChatLog chatLog){

        Game game = gameService.getGame(gameId);

        game.addMessage(chatMode, chatLog);

    }

    public ChatLog getLatestMessage(List<ChatLog> chatLogs){
        if(chatLogs != null) {
            return chatLogs.get(chatLogs.size() - 1);
        }else{
            return new ChatLog(null, null, null, null);
        }
    }

}
