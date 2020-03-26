package ch.uzh.ifi.seal.soprafs20.entity;

/**
 * Temporary format of a chat message.
 */
public class ChatLog {

    private String time, username, message;

    public ChatLog(String time, String username, String message){
        this.time = time;
        this.username = username;
        this.message = message;
    }

    public String getTime(){return time;}

    public String getUsername(){return username;}

    public String getMessage(){return message;}

}
