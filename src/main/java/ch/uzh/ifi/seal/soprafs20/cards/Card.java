package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

public class Card {

    private Suit mySuit;
    private Rank myRank;

    public Card(Suit suit, Rank rank){
        this.mySuit = suit;
        this.myRank = rank;
    }

    public Suit getSuit(){
        return mySuit;
    }

    public Rank getRank(){
        return myRank;
    }

}
