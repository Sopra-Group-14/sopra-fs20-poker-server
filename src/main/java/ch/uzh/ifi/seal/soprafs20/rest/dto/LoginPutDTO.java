package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class LoginPutDTO {
    private String username;
    private String password;

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

