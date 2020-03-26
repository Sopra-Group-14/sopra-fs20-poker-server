package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.chat.Chat;

import java.util.List;

/**
 * Takes care of actions regarding the chat.
 */
public class ChatService {

    public List<String> getHistory(Chat chat){
        return chat.getHistory();
    }

    public void newMessage(Chat chat, String message){
        chat.newMessage(message);
    }

}
