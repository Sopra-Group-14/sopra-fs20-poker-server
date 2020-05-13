package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class ChatGetDTO {

    private long gameId;
    private String chatMode;

    public void setChatMode(String chatMode) {
        this.chatMode = chatMode;
    }

    public String getChatMode() {
        return chatMode;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }
}
