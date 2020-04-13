package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

import java.util.List;

public class CardAnalyser {

    public boolean isRoyalFlush(List<Card> cards){

        boolean hasAce = false;
        boolean hasKing = false;
        boolean hasQueen = false;
        boolean hasJack = false;
        boolean hasTen = false;
        Suit suit = null;

        while(0<cards.size()){
            if(suit == null){
                suit = cards.get(0).getSuit();
            }
            if(cards.get(0).getRank() == Rank.ACE && cards.get(0).getSuit() == suit) {
                hasAce = true;
            }
            if(cards.get(0).getRank() == Rank.KING && cards.get(0).getSuit() == suit){
                hasKing = true;
            }
            if(cards.get(0).getRank() == Rank.QUEEN && cards.get(0).getSuit() == suit){
                hasQueen = true;
            }
            if(cards.get(0).getRank() == Rank.JACK && cards.get(0).getSuit() == suit){
                hasJack = true;
            }
            if(cards.get(0).getRank() == Rank.TEN && cards.get(0).getSuit() == suit){
                hasTen = true;
            }
            cards.remove(0);
        }

        return hasAce && hasKing && hasQueen && hasJack && hasTen;

    }



}

