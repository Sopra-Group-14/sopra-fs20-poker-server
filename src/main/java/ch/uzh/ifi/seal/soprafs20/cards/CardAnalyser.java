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

    public boolean isFourOfAKind(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        int i = 0;

        while(0<cards.size()){
            if(i==0){
                listOne.add(cards.get(i));
            }
            else if(cards.get(0).getRank() == listOne.get(0).getRank()){
                listOne.add(cards.get(0));
            }else{
                if(i>0 && listTwo.size() == 0){
                    listTwo.add(cards.get(0));
                }else{
                    if(cards.get(0).getRank() == listTwo.get(0).getRank()){
                        listTwo.add(cards.get(0));
                    }
                }
            }
            cards.remove(0);
            i+=1;
        }

        return listOne.size() == 4 || listTwo.size() == 4;

    }

    public boolean isFullHouse(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        int i = 0;

        while(0<cards.size()){
            if(i==0){
                listOne.add(cards.get(i));
            }
            else if(cards.get(0).getRank() == listOne.get(0).getRank()){
                listOne.add(cards.get(0));
            }else{
                if(i>0 && listTwo.size() == 0){
                    listTwo.add(cards.get(0));
                }else{
                    if(cards.get(0).getRank() == listTwo.get(0).getRank()){
                        listTwo.add(cards.get(0));
                    }
                }
            }
            cards.remove(0);
            i+=1;
        }

        return (listOne.size() == 2 && listTwo.size() == 3) || (listOne.size() == 3 && listTwo.size() == 2);

    }

    public boolean isFlush(List<Card> cards){

        boolean allSameSuit = true;
        Suit suit = cards.get(0).getSuit();
        cards.remove(0);

        while(0<cards.size()){
            if(cards.get(0).getSuit() != suit){
                allSameSuit = false;
                break;
            }
            cards.remove(0);
        }

        return allSameSuit;

    }

    public boolean isStraight(List<Card> cards){

        //We have to execute the whole thing twice; first to check if the ace (if there is one) is the highest card
        //Second; if the ace is the lowest card
        boolean firstCondition = false;
        boolean secondCondition = false;

        //We backup the cards array for checking after the first condition
        List<Card> cardsTwo = new ArrayList<>(cards);

        boolean hasSecondHighest = false;
        boolean hasThirdHighest = false;
        boolean hasFourthHighest = false;
        boolean hasFifthHighest = false;
        boolean lastCanBeAce = false;

        Card highestCard = highestCard(cards);
        int highestInt = rankToInt(highestCard.getRank());
        cards.remove(highestCard);
        int highestIntTwo = highestInt;

        //This is the second scenario; if the ace is the lowest card
        if(highestCard.getRank() == Rank.ACE){
            Card tempReAdd = highestCard;
            highestCard = highestCard(cards);
            highestInt = rankToInt(highestCard.getRank());
            cards.add(tempReAdd);
        }

        while(0<cards.size()){
            if(rankToInt(cards.get(0).getRank()) == highestInt-1){
                hasSecondHighest = true;
            }
            if(rankToInt(cards.get(0).getRank()) == highestInt-2){
                hasThirdHighest = true;
            }
            if(rankToInt(cards.get(0).getRank()) == highestInt-3){
                hasFourthHighest = true;
                if(cards.get(0).getRank() == Rank.TWO){
                    lastCanBeAce = true;
                }
            }
            if((rankToInt(cards.get(0).getRank()) == highestInt-4) || (lastCanBeAce && cards.get(0).getRank() == Rank.ACE)){
                hasFifthHighest = true;
            }
            cards.remove(0);
        }

        secondCondition = hasSecondHighest && hasThirdHighest && hasFourthHighest && hasFifthHighest;

        //This is the first scenario; if the ace is the highest card

        hasSecondHighest = false;
        hasThirdHighest = false;
        hasFourthHighest = false;
        hasFifthHighest = false;
        lastCanBeAce = false;

        while(0<cardsTwo.size()){
            if(rankToInt(cardsTwo.get(0).getRank()) == highestIntTwo-1){
                hasSecondHighest = true;
            }
            if(rankToInt(cardsTwo.get(0).getRank()) == highestIntTwo-2){
                hasThirdHighest = true;
            }
            if(rankToInt(cardsTwo.get(0).getRank()) == highestIntTwo-3){
                hasFourthHighest = true;
                if(cardsTwo.get(0).getRank() == Rank.TWO){
                    lastCanBeAce = true;
                }
            }
            if((rankToInt(cardsTwo.get(0).getRank()) == highestIntTwo-4) || (lastCanBeAce && cardsTwo.get(0).getRank() == Rank.ACE)){
                hasFifthHighest = true;
            }
            cardsTwo.remove(0);
        }

        firstCondition = hasSecondHighest && hasThirdHighest && hasFourthHighest && hasFifthHighest;

        //In the end, one of the two conditions have to be true
        return firstCondition || secondCondition;

    }

    public boolean isThreeOfAKind(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        List<Card> listThree = new ArrayList<>();
        int i = 0;

        while(0<cards.size()){
            if(i==0){
                listOne.add(cards.get(i));
            }
            else if(cards.get(0).getRank() == listOne.get(0).getRank()){
                listOne.add(cards.get(0));
            }else{
                if(i>0 && listTwo.size() == 0){
                    listTwo.add(cards.get(0));
                }else if(cards.get(0).getRank() == listTwo.get(0).getRank()){
                    listTwo.add(cards.get(0));
                }else{
                    if(i>0 && listThree.size() == 0){
                        listThree.add(cards.get(0));
                    }else if(cards.get(0).getRank() == listThree.get(0).getRank()){
                        listThree.add(cards.get(0));
                    }
                }
            }
            cards.remove(0);
            i+=1;
        }

        return listOne.size() == 3 || listTwo.size() == 3 || listThree.size() == 3;

    }

    public boolean isTwoPairs(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        List<Card> listThree = new ArrayList<>();
        int i = 0;

        while(0<cards.size()){
            if(i==0){
                listOne.add(cards.get(i));
            }
            else if(cards.get(0).getRank() == listOne.get(0).getRank()){
                listOne.add(cards.get(0));
            }else{
                if(i>0 && listTwo.size() == 0){
                    listTwo.add(cards.get(0));
                }else if(cards.get(0).getRank() == listTwo.get(0).getRank()){
                    listTwo.add(cards.get(0));
                }else{
                    if(i>0 && listThree.size() == 0){
                        listThree.add(cards.get(0));
                    }else if(cards.get(0).getRank() == listThree.get(0).getRank()){
                        listThree.add(cards.get(0));
                    }
                }
            }
            cards.remove(0);
            i+=1;
        }

        return (listOne.size() ==2 && listTwo.size() == 2) || (listOne.size() == 2 && listThree.size() == 2) || (listTwo.size() == 2 && listThree.size() == 2);

    }

    public boolean isOnePair(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        List<Card> listThree = new ArrayList<>();
        List<Card> listFour = new ArrayList<>();
        int i = 0;

        while(0<cards.size()){
            if(i==0){
                listOne.add(cards.get(i));
            }
            else if(cards.get(0).getRank() == listOne.get(0).getRank()){
                listOne.add(cards.get(0));
            }else{
                if(i>0 && listTwo.size() == 0){
                    listTwo.add(cards.get(0));
                }else if(cards.get(0).getRank() == listTwo.get(0).getRank()){
                    listTwo.add(cards.get(0));
                }else{
                    if(i>0 && listThree.size() == 0){
                        listThree.add(cards.get(0));
                    }else if(cards.get(0).getRank() == listThree.get(0).getRank()){
                        listThree.add(cards.get(0));
                    }else{
                        if(i>0 && listFour.size() == 0){
                            listFour.add(cards.get(0));
                        }else if(cards.get(0).getRank() == listFour.get(0).getRank()){
                            listFour.add(cards.get(0));
                        }
                    }
                }
            }
            cards.remove(0);
            i+=1;
        }

        return listOne.size() == 2 || listTwo.size() == 2 || listThree.size() == 2 || listFour.size() == 2;

    }

    public boolean isHighCard(List<Card> cards){
        return true;
    }

}

