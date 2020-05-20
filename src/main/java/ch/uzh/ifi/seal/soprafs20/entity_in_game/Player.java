package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.API_Card;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerReadyStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;

import java.util.LinkedList;
import java.util.List;

public class Player{

    private List<Card> hand = new LinkedList<>();
    private List<String> apiHand = new LinkedList<>();

    //private static final int START_CREDIT = 1000;

    private int credit;

    private long id;

    private boolean isHost;

    private int amountInPot = 0;

    private String playerName;

    private boolean thisPlayersTurn = true;

    private boolean folded = false;

    private boolean checked= false;

    API_Card api_card;

    private PlayerReadyStatus readyStatus = PlayerReadyStatus.UNREADY;


    public Player(User user){
        this.credit = user.getBalance();
        this.id = user.getId();
        this.playerName = user.getUsername();
    }

    //Alternate constructor for testing purposes only!
    public Player(String name){
        this.playerName = name;
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
        setApiHand(this.hand);
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

    public void fold(){
        this.setFolded(true);}

    public boolean hasFolded(){return this.isFolded();}

    public void check(){this.checked = true;}

    public boolean hasChecked(){return this.checked;}

    public PlayerReadyStatus getReadyStatus(){return this.readyStatus;}

    public void toggleReadyStatus(){
        if(readyStatus == PlayerReadyStatus.READY){
            readyStatus = PlayerReadyStatus.UNREADY;
        }else{
            readyStatus = PlayerReadyStatus.READY;
        }
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public List<String> getApiHand() {
        return apiHand;
    }

    public void setApiHand(List<Card> hand) {
        this.apiHand.clear();
        for (int i = 0; i < hand.size(); i++) {
            api_card = new API_Card(hand.get(i).getSuit(), hand.get(i).getRank());
            this.apiHand.add(api_card.getApiCard());
        }
    }
}
