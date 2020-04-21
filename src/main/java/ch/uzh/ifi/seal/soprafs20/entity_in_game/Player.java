package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;

import java.util.LinkedList;
import java.util.List;

public class Player{

    private List<Card> hand = new LinkedList<>();

    private static final int START_CREDIT = 1000;

    private int credit;

    private long id;

    private boolean isHost;

    private int amountInPot = 0;

    private String playerName;

    private boolean thisPlayersTurn = true;

    private boolean folded = false;

    private boolean checked= false;


    public Player(User user){
        this.credit = START_CREDIT;
        this.id = user.getId();
        this.playerName = user.getUsername();
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

    public int getAmountInPot(){return amountInPot;}

    public String getPlayerName(){return this.playerName;}

    public void setAmountInPot(int amountInPot){
        this.amountInPot = amountInPot;
    }

    public void setThisPlayersTurn(boolean thisPlayersTurn){this.thisPlayersTurn = thisPlayersTurn;}

    public boolean isThisPlayersTurn(){return this.thisPlayersTurn;}

    public void fold(){this.folded = true;}

    public boolean hasFolded(){return this.folded;}

    public void check(){this.checked = true;}

    public boolean hasChecked(){return this.checked;}

}
