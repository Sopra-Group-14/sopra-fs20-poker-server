package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserMode;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import jdk.net.SocketFlow;

public class JoinGameDTO {

        private long userId;

        private String userMode;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setUserMode(String userMode){this.userMode = userMode;}

        public String getUserMode(){return userMode;}
}
