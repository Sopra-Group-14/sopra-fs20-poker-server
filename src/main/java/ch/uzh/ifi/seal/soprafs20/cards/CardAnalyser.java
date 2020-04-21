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

    public PokerHand getPokerHand(List<Card> cards){

        List<Card> newCards = new ArrayList<>(cards);
        PokerHand royalFlush = isRoyalFlush(newCards);
        if(royalFlush != null){
            return royalFlush;
        }

        newCards = new ArrayList<>(cards);
        PokerHand straightFlush = isStraightFlush(newCards);
        if(straightFlush != null){
            return straightFlush;
        }

        newCards = new ArrayList<>(cards);
        PokerHand fourOfAKind = isFourOfAKind(newCards);
        if(fourOfAKind != null){
            return fourOfAKind;
        }

        newCards = new ArrayList<>(cards);
        PokerHand fullHouse = isFullHouse(newCards);
        if(fullHouse != null){
            return fullHouse;
        }

        newCards = new ArrayList<>(cards);
        PokerHand flush = isFlush(newCards);
        if(flush != null){
            return flush;
        }

        newCards = new ArrayList<>(cards);
        PokerHand straight = isStraight(newCards);
        if(straight != null){
            return straight;
        }

        newCards = new ArrayList<>(cards);
        PokerHand threeOfAKind = isThreeOfAKind(newCards);
        if(threeOfAKind != null){
            return threeOfAKind;
        }

        newCards = new ArrayList<>(cards);
        PokerHand twoPairs = isTwoPairs(newCards);
        if(twoPairs != null) {
            return twoPairs;
        }

        newCards = new ArrayList<>(cards);
        PokerHand onePair = isOnePair(newCards);
        if(onePair != null){
            return onePair;
        }

        newCards = new ArrayList<>(cards);
        PokerHand highCard = isHighCard(newCards);
        if(highCard != null){
            return highCard;
        }

        return new PokerHand(0, null, null, null, null, null);

    }

    private Card highestCard(List<Card> cards){

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

    private Rank intToRank(int integer){

        return ranks.get(integer);

    }

    private int[] sortList(int[] list){
        int i, e, temp;

        for(i = 0;i<list.length-1; i++){
            for(e = 0;e<list.length-1;e++){
                if(list[e] < list[e+1]){
                    temp = list[e];
                    list[e] = list[e+1];
                    list[e+1] = temp;
                }
            }
        }

        return list;

    }

    private PokerHand isRoyalFlush(List<Card> cards){

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

        if(hasAce && hasKing && hasQueen && hasJack && hasTen){
            return new PokerHand(10, null, null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isStraightFlush(List<Card> cards){

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

        if(hasSecondHighest && hasThirdHighest && hasFourthHighest && hasFifthHighest){
            return new PokerHand(9, highestCard.getRank(), null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isFourOfAKind(List<Card> cards){

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

        if(listOne.size() == 4){
            return new PokerHand(8, listOne.get(0).getRank(), null, null, null, null);
        }else if(listTwo.size() == 4){
            return new PokerHand(8, listTwo.get(0).getRank(), null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isFullHouse(List<Card> cards){

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

        if(listOne.size() == 2 && listTwo.size() == 3){
            return new PokerHand(7, listTwo.get(0).getRank(), listOne.get(0).getRank(), null, null, null);
        }else if(listOne.size() == 3 && listTwo.size() == 2){
            return new PokerHand(7, listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isFlush(List<Card> cards){

        int[] rankList = {0,0,0,0,0};
        int i, e, temp;
        boolean allSameSuit = true;
        Suit suit = cards.get(0).getSuit();
        rankList[0] = rankToInt(cards.get(0).getRank());
        cards.remove(0);

        i=1;
        while(0<cards.size()){
            if(cards.get(0).getSuit() != suit){
                allSameSuit = false;
                break;
            }
            rankList[i]= (rankToInt(cards.get(0).getRank()));
            cards.remove(0);
            i+=1;
        }

        //Sort RankList
        rankList = sortList(rankList);

        if(allSameSuit){
            return new PokerHand(6, intToRank(rankList[0]), intToRank(rankList[1]), intToRank(rankList[2]), intToRank(rankList[3]), intToRank(rankList[4]));
        }else{
            return null;
        }

    }

    private PokerHand isStraight(List<Card> cards){

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
        if(secondCondition){
            return new PokerHand(5, highestCard.getRank(), null, null, null, null);
        }else if(firstCondition){
            return new PokerHand(5, intToRank(highestIntTwo), null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isThreeOfAKind(List<Card> cards){

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


        if(listOne.size() == 3){
            if(rankToInt(listTwo.get(0).getRank())>rankToInt(listThree.get(0).getRank())){
                return new PokerHand(4, listOne.get(0).getRank(), listTwo.get(0).getRank(), listThree.get(0).getRank(), null, null);
            }else{
                return new PokerHand(4, listOne.get(0).getRank(), listThree.get(0).getRank(), listTwo.get(0).getRank(), null, null);
            }
        }else if(listTwo.size() == 3){
            if(rankToInt(listOne.get(0).getRank())>rankToInt(listThree.get(0).getRank())){
                return new PokerHand(4, listTwo.get(0).getRank(), listOne.get(0).getRank(), listThree.get(0).getRank(), null, null);
            }else{
                return new PokerHand(4, listTwo.get(0).getRank(), listThree.get(0).getRank(), listOne.get(0).getRank(), null, null);
            }
        }else if(listThree.size() == 3){
            if(rankToInt(listTwo.get(0).getRank())>rankToInt(listOne.get(0).getRank())){
                return new PokerHand(4, listThree.get(0).getRank(), listTwo.get(0).getRank(), listOne.get(0).getRank(), null, null);
            }else{
                return new PokerHand(4, listThree.get(0).getRank(), listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null);
            }
        }else{
            return null;
        }

    }

    private PokerHand isTwoPairs(List<Card> cards){

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

        if(listOne.size() ==2 && listTwo.size() == 2){
            if(rankToInt(listOne.get(0).getRank())>rankToInt(listTwo.get(0).getRank())){
                return new PokerHand(3, listOne.get(0).getRank(), listTwo.get(0).getRank(), listThree.get(0).getRank(), null, null);
            }else{
                return new PokerHand(3, listTwo.get(0).getRank(), listOne.get(0).getRank(), listThree.get(0).getRank(), null, null);
            }
        }else if(listOne.size() == 2 && listThree.size() == 2){
            if(rankToInt(listOne.get(0).getRank())>rankToInt(listThree.get(0).getRank())){
                return new PokerHand(3, listOne.get(0).getRank(), listThree.get(0).getRank(), listTwo.get(0).getRank(), null, null);
            }else{
                return new PokerHand(3, listThree.get(0).getRank(), listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null);
            }
        }else if(listTwo.size() == 2 && listThree.size() == 2){
            if(rankToInt(listThree.get(0).getRank())>rankToInt(listTwo.get(0).getRank())){
                return new PokerHand(3, listThree.get(0).getRank(), listTwo.get(0).getRank(), listOne.get(0).getRank(), null, null);
            }else{
                return new PokerHand(3, listTwo.get(0).getRank(), listThree.get(0).getRank(), listOne.get(0).getRank(), null, null);
            }
        }else{
            return null;
        }

    }

    private PokerHand isOnePair(List<Card> cards){

        List<Card> listOne = new ArrayList<>();
        List<Card> listTwo = new ArrayList<>();
        List<Card> listThree = new ArrayList<>();
        List<Card> listFour = new ArrayList<>();
        int[] kickerInts = {0,0,0};
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

        if(listOne.size() == 2){
            kickerInts[0] = rankToInt(listTwo.get(0).getRank());
            kickerInts[1] = rankToInt(listThree.get(0).getRank());
            kickerInts[2] = rankToInt(listFour.get(0).getRank());

            kickerInts = sortList(kickerInts);

            return new PokerHand(2, listOne.get(0).getRank(), intToRank(kickerInts[0]), intToRank(kickerInts[1]), intToRank(kickerInts[2]), null);

        }else if(listTwo.size() == 2){
            kickerInts[0] = rankToInt(listOne.get(0).getRank());
            kickerInts[1] = rankToInt(listThree.get(0).getRank());
            kickerInts[2] = rankToInt(listFour.get(0).getRank());

            kickerInts = sortList(kickerInts);

            return new PokerHand(2, listTwo.get(0).getRank(), intToRank(kickerInts[0]), intToRank(kickerInts[1]), intToRank(kickerInts[2]), null);

        }else if(listThree.size() == 2){
            kickerInts[0] = rankToInt(listTwo.get(0).getRank());
            kickerInts[1] = rankToInt(listOne.get(0).getRank());
            kickerInts[2] = rankToInt(listFour.get(0).getRank());

            kickerInts = sortList(kickerInts);

            return new PokerHand(2, listThree.get(0).getRank(), intToRank(kickerInts[0]), intToRank(kickerInts[1]), intToRank(kickerInts[2]), null);

        }else if(listFour.size() == 2){
            kickerInts[0] = rankToInt(listTwo.get(0).getRank());
            kickerInts[1] = rankToInt(listThree.get(0).getRank());
            kickerInts[2] = rankToInt(listOne.get(0).getRank());

            kickerInts = sortList(kickerInts);

            return new PokerHand(2, listFour.get(0).getRank(), intToRank(kickerInts[0]), intToRank(kickerInts[1]), intToRank(kickerInts[2]), null);

        }else{
            return null;
        }

    }

    private PokerHand isHighCard(List<Card> cards){

        int[] rankInts = {0,0,0,0,0};
        int i;

        for(i=0;i<cards.size();i++){
            rankInts[i] = rankToInt(cards.get(i).getRank());
        }

        rankInts = sortList(rankInts);

        return new PokerHand(1, intToRank(rankInts[0]), intToRank(rankInts[1]), intToRank(rankInts[2]), intToRank(rankInts[3]), intToRank(rankInts[4]));

    }


}


