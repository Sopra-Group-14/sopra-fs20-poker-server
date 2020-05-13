package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;

import java.util.Date;

public class ChatPutDTO {

    private String username;
    private String chatMode;
    private String timeStamp;
    private String message;
    private long gameId;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setChatMode(String chatMode) {
        this.chatMode = chatMode;
    }

    public String getChatMode() {
        return chatMode;
    }

    public ChatLog getChatLog(){
        ChatLog chatLog = new ChatLog(this.timeStamp, this.username, this.message, this.chatMode);
        return chatLog;
    }
}
