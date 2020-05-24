package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ChatPutDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Takes care of actions regarding the chat.
 */
@Service
@Transactional
public class ChatService {

    private final GameService gameService;
    private final UserService userService;

    public ChatService(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public List<ChatLog> getHistory(String chatMode, long gameId){

        Game game = gameService.getGame(gameId);

        return game.getMessages(chatMode);
    }

    public void newMessage(String chatMode, long gameId, ChatLog chatLog){

        Game game = gameService.getGame(gameId);

        game.addMessage(chatMode, chatLog);

    }

    public ChatLog chatPutDTOtoChatLog(ChatPutDTO chatPutDTO, String source, String chatMode){

        String userName;
        long userId = chatPutDTO.getUserId();

        if(source.equals("player")) {

            userName = userService.getUserById(userId).getUsername();

        }else{
            userName = "Spectator" + userId;

        }

        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
        String message = chatPutDTO.getMessage();
        return new ChatLog(timeStamp, userName, Long.toString(userId), message, chatMode);

    }

    public ChatLog getLatestMessage(List<ChatLog> chatLogs){
        if (chatLogs == null || chatLogs.isEmpty()) {
            return new ChatLog(null, null, null, null, null);
        }
        else {
            return chatLogs.get(chatLogs.size() - 1);
        }
    }

}
