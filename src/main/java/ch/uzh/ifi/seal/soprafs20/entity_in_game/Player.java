package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;

import java.util.List;

public class Player{

    private List<Card> hand;

    private static final int START_CREDIT = 1000;

    private int credit;

    private long id;

    private boolean isHost;

    public Player(long id){
        this.credit = START_CREDIT;
        this.id = id;
    }


    public void addCredit(int amount){
        credit += amount;
    }

    public void removeCredit(int amount){
        credit -= amount;
    }

    public int getCredit(){
        return credit;
    }

    public void addToHand(Card card){
        hand.add(card);
    }

    public List<Card> getHand(){
        return hand;
    }

    public long getId(){
        return id;
    }

}
