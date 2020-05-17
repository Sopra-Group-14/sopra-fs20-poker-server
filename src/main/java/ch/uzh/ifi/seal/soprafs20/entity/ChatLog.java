package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;

/**
 * Temporary format of a chat message.
 */
public class ChatLog {

    private String time, username, message, chatMode, userId;

    public ChatLog(String time, String username, String userId, String message, String chatMode){
        this.time = time;
        this.username = username;
        this.userId = userId;
        this.message = message;
        this.chatMode = chatMode;
    }

    public String getTime(){return time;}

    public String getUsername(){return username;}

    public String getMessage(){return message;}

    public String getUserId() {
        return userId;
    }
}
