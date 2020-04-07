package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;

import java.util.List;

public class Player{

    private List<Card> hand;

    private int credit;

    private long id;

    Player(int credit, long id){
        this.credit = credit;
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
