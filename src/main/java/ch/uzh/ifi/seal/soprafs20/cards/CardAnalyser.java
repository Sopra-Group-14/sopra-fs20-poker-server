package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

import java.util.ArrayList;
import java.util.List;

public class CardAnalyser {

    private List<Rank> ranks = new ArrayList<>();

    public CardAnalyser(){
        ranks.add(Rank.TWO);
        ranks.add(Rank.THREE);
        ranks.add(Rank.FOUR);
        ranks.add(Rank.FIVE);
        ranks.add(Rank.SIX);
        ranks.add(Rank.SEVEN);
        ranks.add(Rank.EIGHT);
        ranks.add(Rank.NINE);
        ranks.add(Rank.TEN);
        ranks.add(Rank.JACK);
        ranks.add(Rank.QUEEN);
        ranks.add(Rank.KING);
        ranks.add(Rank.ACE);
    }

    public Card highestCard(List<Card> cards){

        Card highest = cards.get(0);
        int i;

        for(i=0;i<cards.size();i++){
            if(rankToInt(cards.get(i).getRank()) > rankToInt(highest.getRank())){
                highest = cards.get(i);
            }
        }

        return highest;

    }

    public int rankToInt(Rank rank){

        int rankInt = -1;
        int i;

        for(i=0;i<ranks.size();i++){
            if(ranks.get(i) == rank){
                rankInt = i;
            }
        }

        return rankInt;

    }

    public Rank intToRank(int integer){

        return ranks.get(integer);

    }

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

    public boolean isStraightFlush(List<Card> cards){

        boolean hasSecondHighest = false;
        boolean hasThirdHighest = false;
        boolean hasFourthHighest = false;
        boolean hasFifthHighest = false;
        boolean lastCanBeAce = false;

        Card highestCard = highestCard(cards);
        int highestInt = rankToInt(highestCard.getRank());
        Suit suit = highestCard.getSuit();
        cards.remove(highestCard);

        //If the highest card is an ace and there is a flush, the royal flush method would have caught it.
        //Not the only scenario is that ace is the lowest card, in which case it cannot be the highest.
        if(highestCard.getRank() == Rank.ACE){
            Card tempReAdd = highestCard;
            highestCard = highestCard(cards);
            highestInt = rankToInt(highestCard.getRank());
            cards.add(tempReAdd);
        }

        while(0<cards.size()){
            if(rankToInt(cards.get(0).getRank()) == highestInt-1 && cards.get(0).getSuit() == suit){
                hasSecondHighest = true;
            }
            if(rankToInt(cards.get(0).getRank()) == highestInt-2 && cards.get(0).getSuit() == suit){
                hasThirdHighest = true;
            }
            if(rankToInt(cards.get(0).getRank()) == highestInt-3 && cards.get(0).getSuit() == suit){
                hasFourthHighest = true;
                if(cards.get(0).getRank() == Rank.TWO){
                    lastCanBeAce = true;
                }
            }
            if((rankToInt(cards.get(0).getRank()) == highestInt-4 && cards.get(0).getSuit() == suit) || (lastCanBeAce && cards.get(0).getRank() == Rank.ACE && cards.get(0).getSuit() == suit)){
                hasFifthHighest = true;
            }
            cards.remove(0);
        }

        return hasSecondHighest && hasThirdHighest && hasFourthHighest && hasFifthHighest;

    }

}

