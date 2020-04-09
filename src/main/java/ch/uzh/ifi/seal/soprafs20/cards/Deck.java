package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

    private java.util.List<Card> cards = new ArrayList<>();

    public Deck(){
        for (Suit s: Suit.values()){
            for (Rank r: Rank.values()){
                Card c = new Card(s, r);
                cards.add(c);
            }
        }
    }

        public java.util.List<Card> getCards(){
            return new ArrayList<>(cards);
        }

        public void shuffle(){
            Collections.shuffle(this.cards);
        }

        public Card getTopCard(){
            Random rand = new Random();
            Card topCard = this.cards.get(rand.nextInt(this.cards.size()));
            return new Card(topCard.getSuit(), topCard.getRank());
        }

}
