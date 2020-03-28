package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.chat.Chat;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Takes care of actions regarding the chat.
 */
@Service
@Transactional
public class ChatService {


    public List<String> getHistory(String chatmode, long gameId){
        return null;
    }

    public void newMessage(Chat chat, String message){
        chat.newMessage(message);
    }

}
