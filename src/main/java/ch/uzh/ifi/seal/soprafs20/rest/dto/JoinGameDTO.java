package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import jdk.net.SocketFlow;

public class JoinGameDTO {

        private long userId;

        private UserStatus userStatus;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setUserStatus(UserStatus userStatus){this.userStatus = userStatus;}

        public UserStatus getUserStatus(){return userStatus;}
}
