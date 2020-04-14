package ch.uzh.ifi.seal.soprafs20.entity_in_game;
import ch.uzh.ifi.seal.soprafs20.entity.User;

public class BigBlind extends Player {
    int credit;
    //TODO deleted credit from constructor handle credit with update credit
    BigBlind(int credit, User user) {

        super(user);
        this.credit = credit;
    }
    public int getCredit(){return this.credit;}
}
