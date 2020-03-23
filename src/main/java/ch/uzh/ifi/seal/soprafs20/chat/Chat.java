package ch.uzh.ifi.seal.soprafs20.chat;
import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;

import java.util.List;

/**
 * Chat superclass; holds everything that is relevant for both player and spectator chat.
 */
public class Chat {

    private List<String> history;


    public void newMessage(String message){
        history.add(message);
    }

    public List<String> getHistory(){
        return history;
    }

    public String getLastMessage(){
        return history.get(history.size()-1);
    }

    private void chatLogToString(ChatLog chatLog){}

}
