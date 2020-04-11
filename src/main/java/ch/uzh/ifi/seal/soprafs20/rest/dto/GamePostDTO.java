package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class GamePostDTO {

    private String gameName;
    private long gameHostID;
    private String potType;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameHostID(long gameHostID) {
        this.gameHostID = gameHostID;
    }

    public long getGameHostID() {
        return gameHostID;
    }

    public void setPotType(String potType) {
        this.potType = potType;
    }

    public String getPotType() {
        return potType;
    }
}
