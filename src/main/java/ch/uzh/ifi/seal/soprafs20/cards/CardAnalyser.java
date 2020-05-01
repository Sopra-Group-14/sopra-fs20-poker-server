package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

import java.util.ArrayList;
import java.util.LinkedList;
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

        return new PokerHand(0, null, null, null, null, null, null, null);

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

    private List<List<Card>> makeSuitLists(List<Card> cards){

        List<Card> firstSuit = new LinkedList<>();
        List<Card> secondSuit = new LinkedList<>();
        List<Card> thirdSuit = new LinkedList<>();
        List<Card> fourthSuit = new LinkedList<>();
        List<List<Card>> masterList = new LinkedList<>();

        while(0<cards.size()){
            if(firstSuit.size() == 0 || cards.get(0).getSuit() == firstSuit.get(0).getSuit()){
                firstSuit.add(cards.get(0));
            }else if(secondSuit.size() == 0 || cards.get(0).getSuit() == secondSuit.get(0).getSuit()){
                secondSuit.add(cards.get(0));
            }else{
                if(thirdSuit.size() == 0 || cards.get(0).getSuit() == thirdSuit.get(0).getSuit()){
                    thirdSuit.add(cards.get(0));
                }else{
                    fourthSuit.add(cards.get(0));
                }
            }
            cards.remove(0);
        }

        masterList.add(firstSuit);
        masterList.add(secondSuit);
        masterList.add(thirdSuit);
        masterList.add(fourthSuit);

        return masterList;

    }

    private List<List<Card>> makeRankLists(List<Card> cards){

        List<Card> firstRank = new LinkedList<>();
        List<Card> secondRank = new LinkedList<>();
        List<Card> thirdRank = new LinkedList<>();
        List<Card> fourthRank = new LinkedList<>();
        List<Card> fifthRank = new LinkedList<>();
        List<Card> sixthRank = new LinkedList<>();
        List<List<Card>> masterList = new LinkedList<>();

        while(0<cards.size()){
            if(firstRank.size() == 0 || cards.get(0).getRank() == firstRank.get(0).getRank()){
                firstRank.add(cards.get(0));
            }else if(secondRank.size() == 0 || cards.get(0).getRank() == secondRank.get(0).getRank()){
                secondRank.add(cards.get(0));
            }else{
                if(thirdRank.size() == 0 || cards.get(0).getRank() == thirdRank.get(0).getRank()){
                    thirdRank.add(cards.get(0));
                }else{
                    if(fourthRank.size() == 0 || cards.get(0).getRank() == fourthRank.get(0).getRank()){
                        fourthRank.add(cards.get(0));
                    }else{
                        if(fifthRank.size() == 0 || cards.get(0).getRank() == fifthRank.get(0).getRank()){
                            fifthRank.add(cards.get(0));
                        }else{
                            if(sixthRank.size() == 0 || cards.get(0).getRank() == sixthRank.get(0).getRank()){
                                sixthRank.add(cards.get(0));
                            }
                        }
                    }
                }
            }
            cards.remove(0);
        }

        masterList.add(firstRank);
        masterList.add(secondRank);
        masterList.add(thirdRank);
        masterList.add(fourthRank);
        masterList.add(fifthRank);
        masterList.add(sixthRank);

        return masterList;

    }

    private PokerHand isRoyalFlush(List<Card> cards){

        boolean hasAce = false;
        boolean hasKing = false;
        boolean hasQueen = false;
        boolean hasJack = false;
        boolean hasTen = false;

        List<List<Card>> masterList = makeSuitLists(cards);
        List<Card> firstSuit = masterList.get(0);
        List<Card> secondSuit = masterList.get(1);
        List<Card> thirdSuit = masterList.get(2);

        List<Card> potentialList = new LinkedList<>();

        if(firstSuit.size() > 4){
            potentialList = firstSuit;
        }
        if(secondSuit.size() > 4){
            potentialList = secondSuit;
        }
        if(thirdSuit.size() > 4){
            potentialList = thirdSuit;
        }

        while(0<potentialList.size()){
            if(potentialList.get(0).getRank() == Rank.ACE) {
                hasAce = true;
            }
            if(potentialList.get(0).getRank() == Rank.KING){
                hasKing = true;
            }
            if(potentialList.get(0).getRank() == Rank.QUEEN){
                hasQueen = true;
            }
            if(potentialList.get(0).getRank() == Rank.JACK){
                hasJack = true;
            }
            if(potentialList.get(0).getRank() == Rank.TEN){
                hasTen = true;
            }
            potentialList.remove(0);
        }

        if(hasAce && hasKing && hasQueen && hasJack && hasTen){
            return new PokerHand(10, null, null, null, null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isStraightFlush(List<Card> cards){

        List<List<Card>> masterList = makeSuitLists(cards);
        List<Card> firstSuit = masterList.get(0);
        List<Card> secondSuit = masterList.get(1);
        List<Card> thirdSuit = masterList.get(2);


        List<Card> potentialList = new LinkedList<>();

        if(firstSuit.size() > 4){
            potentialList = firstSuit;
        }
        if(secondSuit.size() > 4){
            potentialList = secondSuit;
        }
        if(thirdSuit.size() > 4){
            potentialList = thirdSuit;
        }

        if(firstSuit.size() < 5 && secondSuit.size() < 5 && thirdSuit.size() < 5){
            return null;
        }
        if(potentialList.size() == 5){
            return calculateStraightFlush(potentialList);
        }

        //Make an integer list
        int[] intList;

        if(potentialList.size() == 6){
            intList = new int[]{0, 0, 0, 0, 0, 0};
        }else{
            intList = new int[]{0, 0, 0, 0, 0, 0, 0};
        }

        //Transcribe the potentialList into an integer list based on card ranks
        int i;

        for(i=0;i<potentialList.size();i++){
            intList[i] = rankToInt(potentialList.get(i).getRank());
        }

        //Sort the integer list
        sortList(intList);

        //Convert the integer back to a card list (with an arbitrary suit, it doesn't matter)
        //Now we have a card list that is sorted in descending order based on rank
        List<Card> newCards = new LinkedList<>();
        for(i=0;i<intList.length;i++){
            newCards.add(new Card(Suit.CLUBS, intToRank(intList[i])));
        }

        //Here we filter out any duplicates, otherwise the code won't work!
        i=0;
        while(i<newCards.size()){
            if(i != 0 && newCards.get(i).getRank() == newCards.get(i-1).getRank()){
                newCards.remove(i);
            }else{
                i+=1;
            }
        }

        //If after the filtering newCards has a length below 5, we return null
        if(newCards.size() < 5){
            return null;
        }

        //If the list's size is 5, there is only 1 5-sized list that could be a straight flush
        if(newCards.size() == 5){
            return calculateStraightFlush(newCards);
        }

        //If the list's size is 6, there are two 5-sized lists in it with potentially consecutive card ranks
        if(newCards.size() == 6){

            List<Card> leftList = new LinkedList<>();
            leftList.add(newCards.get(0));
            leftList.add(newCards.get(1));
            leftList.add(newCards.get(2));
            leftList.add(newCards.get(3));
            leftList.add(newCards.get(4));

            List<Card> rightList = new LinkedList<>();
            rightList.add(newCards.get(1));
            rightList.add(newCards.get(2));
            rightList.add(newCards.get(3));
            rightList.add(newCards.get(4));
            rightList.add(newCards.get(5));

            //The left list has higher ranks, so if it is a Straight Flush, it takes priority
            PokerHand isLeftList = calculateStraightFlush(leftList);
            if(isLeftList != null){
                return isLeftList;
            }else{
                PokerHand isRightList = calculateStraightFlush(rightList);
                if(isRightList != null){
                    return isRightList;
                }
            }

        }
        //If the list's size is 7, there are three 5-sized lists in it with potentially consecutive card ranks
        if(newCards.size() == 7){

            List<Card> leftList = new LinkedList<>();
            leftList.add(newCards.get(0));
            leftList.add(newCards.get(1));
            leftList.add(newCards.get(2));
            leftList.add(newCards.get(3));
            leftList.add(newCards.get(4));

            List<Card> middleList = new LinkedList<>();
            middleList.add(newCards.get(1));
            middleList.add(newCards.get(2));
            middleList.add(newCards.get(3));
            middleList.add(newCards.get(4));
            middleList.add(newCards.get(5));

            List<Card> rightList = new LinkedList<>();
            rightList.add(newCards.get(2));
            rightList.add(newCards.get(3));
            rightList.add(newCards.get(4));
            rightList.add(newCards.get(5));
            rightList.add(newCards.get(6));

            //Here, the priorities are left->middle->right
            PokerHand isLeftList = calculateStraightFlush(leftList);
            if(isLeftList != null){
                return isLeftList;
            }else{
                PokerHand isMiddleList = calculateStraightFlush(middleList);
                if(isMiddleList != null){
                    return isMiddleList;
                }else{
                    PokerHand isRightList = calculateStraightFlush(rightList);
                    if(isRightList != null){
                        return isRightList;
                    }
                }
            }

        }

        return null;

    }

    private PokerHand calculateStraightFlush(List<Card> cards){

        boolean hasSecondHighest = false;
        boolean hasThirdHighest = false;
        boolean hasFourthHighest = false;
        boolean hasFifthHighest = false;
        boolean lastCanBeAce = false;

        Card highestCard = highestCard(cards);
        int highestInt = rankToInt(highestCard.getRank());
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

        if(hasSecondHighest && hasThirdHighest && hasFourthHighest && hasFifthHighest){
            return new PokerHand(9, highestCard.getRank(), null, null, null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isFourOfAKind(List<Card> cards){

        List<List<Card>> masterList = makeRankLists(cards);
        List<Card> listOne = masterList.get(0);
        List<Card> listTwo = masterList.get(1);
        List<Card> listThree = masterList.get(2);
        List<Card> listFour = masterList.get(3);

        if(listOne.size() == 4){
            return new PokerHand(8, listOne.get(0).getRank(), null, null, null, null, null, null);
        }
        if(listTwo.size() == 4){
            return new PokerHand(8, listTwo.get(0).getRank(), null, null, null, null, null, null);
        }
        if(listThree.size() == 4){
            return new PokerHand(8, listThree.get(0).getRank(), null, null, null, null, null, null);
        }
        if(listFour.size() == 4){
            return new PokerHand(8, listFour.get(0).getRank(), null, null, null, null, null, null);
        }

        return null;

    }

    private PokerHand isFullHouse(List<Card> cards){

        List<List<Card>> masterList = makeRankLists(cards);
        List<Card> listOne = new LinkedList<>();
        List<Card> listTwo = new LinkedList<>();
        List<Card> listThree = new LinkedList<>();

        int i;
        for(i=0;i<masterList.size();i++){
            if(masterList.get(i).size() == 3 && listOne.size() == 0){
                listOne = masterList.get(i);
            }else if(masterList.get(i).size() >= 2 && listTwo.size() == 0){
                listTwo = masterList.get(i);
            }else{
                if(masterList.get(i).size() == 2){
                    listThree = masterList.get(i);
                }
            }
        }

        //There are three scenarios that we have to handle:
        //1: Lengths 3, 2, 1, 1 -> trivial
        //2: Two of length 3 and one of length 1 -> Check which with 3 has highest rank and make the other one the 2-pair
        //3: One of length 3 and two of length 2 -> Check which with length 2 has the highest rank -> make that the 2-pair

        //First scenario
        if(listOne.size() == 3 && listTwo.size() == 2 && listThree.size() == 0){
            return new PokerHand(7, listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null, null, null, null);
        }

        //Second scenario
        if(listOne.size() == 3 && listTwo.size() == 3 && listThree.size() == 0){
            if(rankToInt(listOne.get(0).getRank()) > rankToInt(listTwo.get(0).getRank())){
                return new PokerHand(7, listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null, null, null, null);
            }else{
                return new PokerHand(7, listTwo.get(0).getRank(), listOne.get(0).getRank(), null, null, null, null, null);
            }
        }

        //Third scenario
        if(listOne.size() == 3 && listTwo.size() == 2 && listThree.size() == 2){
            if(rankToInt(listTwo.get(0).getRank()) > rankToInt(listThree.get(0).getRank())){
                return new PokerHand(7, listOne.get(0).getRank(), listTwo.get(0).getRank(), null, null, null, null, null);
            }else{
                return new PokerHand(7, listOne.get(0).getRank(), listThree.get(0).getRank(), null, null, null, null, null);
            }
        }

        return null;

    }

    private PokerHand isFlush(List<Card> cards){

        int[] rankList = {0,0,0,0,0,0,0};

        List<List<Card>> masterList = makeSuitLists(cards);
        List<Card> actualList = new LinkedList<>();

        int i;
        for(i=0;i<3;i++){
            if(masterList.get(i).size() >= 5){
                actualList = masterList.get(i);
            }
        }

        if(actualList.size() >= 5) {

            for (i = 0; i < actualList.size(); i++) {
                rankList[i] = rankToInt(actualList.get(i).getRank());
            }

            //Sort RankList
            sortList(rankList);

            return new PokerHand(6, intToRank(rankList[0]), intToRank(rankList[1]), intToRank(rankList[2]), intToRank(rankList[3]), intToRank(rankList[4]), null, null);

        }

        return null;

    }

    private PokerHand isStraight(List<Card> cards){

        int[] intList = {-1,-1,-1,-1,-1,-1,-1};
        int i;

        //Make an int list based on the ranks of the provided cards
        for(i=0;i<7;i++){
            intList[i] = rankToInt(cards.get(i).getRank());
        }

        //Sort the list
        sortList(intList);

        //We convert the intList back into a Card list
        List<Card> fullList = new LinkedList<>();
        for(i=0;i<7;i++){
            fullList.add(new Card(Suit.CLUBS, intToRank(intList[i])));
        }

        //Now we make three card lists (left, middle, right)
        List<Card> leftList = new LinkedList<>();
        List<Card> middleList = new LinkedList<>();
        List<Card> rightList = new LinkedList<>();

        //Special case: If there is an ace in the list, the sortList function will automatically deem it the highest card
        //However, it can also be the lowest in the straight
        //So, if there is an ace, we make an additional list where it is the lowest card
        List<Card> bottomAceList = new LinkedList<>();
        if(intToRank(intList[0]) == Rank.ACE){
            int e;

            for(e=3;e<7;e++){
                bottomAceList.add(new Card(Suit.CLUBS, intToRank(intList[e])));
            }
            bottomAceList.add(new Card(Suit.CLUBS, Rank.ACE));
        }

        //Now we have to filter out any duplicate cards in the fullList, otherwise the method won't work!
        i=0;
        while(i<fullList.size()){
            if(i != 0 && fullList.get(i).getRank() == fullList.get(i-1).getRank()){
                fullList.remove(i);
            }else{
                i+=1;
            }
        }

        //Depending on the size of fullList now, we will have three scenarios:
        //1: Size = 5 (Two duplicates or a triplet) -> Only leftList
        //2: Size = 6 (One duplicate) -> leftList and middleList
        //3: Size = 7 (No duplicates) -> leftList, middleList, and rightList

        //Case 1
        if(fullList.size() == 5){
            leftList = fullList;
        }

        //Case 2
        if(fullList.size() == 6){
            leftList.add(fullList.get(0));

            for(i=1;i<5;i++){
                leftList.add(fullList.get(i));
                middleList.add(fullList.get(i));
            }

            middleList.add(fullList.get(5));
        }

        //Case 3
        if(fullList.size() == 7){
            leftList.add(fullList.get(0));
            leftList.add(fullList.get(1));
            middleList.add(fullList.get(1));

            for(i=2;i<5;i++){
                leftList.add(fullList.get(i));
                middleList.add(fullList.get(i));
                rightList.add(fullList.get(i));
            }

            middleList.add(fullList.get(5));
            rightList.add(fullList.get(5));
            rightList.add(fullList.get(6));

        }


        //Now we check if the lists are a straight: Priority is left->middle->right->bottomAce
        if(leftList.size() != 0) {
            PokerHand isLeftList = calculateStraight(leftList);
            if (isLeftList != null) {
                return isLeftList;
            } else {
                if(middleList.size() != 0) {
                    PokerHand isMiddleList = calculateStraight(middleList);
                    if (isMiddleList != null) {
                        return isMiddleList;
                    } else {
                        if(rightList.size() != 0) {
                            PokerHand isRightList = calculateStraight(rightList);
                            if (isRightList != null) {
                                return isRightList;
                            } else {
                                if (bottomAceList.size() != 0) {
                                    PokerHand isBottomAceList = calculateStraight(bottomAceList);
                                    if (isBottomAceList != null) {
                                        return isBottomAceList;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;

    }

    private PokerHand calculateStraight(List<Card> cards){

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
            return new PokerHand(5, highestCard.getRank(), null, null, null, null, null, null);
        }else if(firstCondition){
            return new PokerHand(5, intToRank(highestIntTwo), null, null, null, null, null, null);
        }else{
            return null;
        }

    }

    private PokerHand isThreeOfAKind(List<Card> cards){

        List<List<Card>> masterList = makeRankLists(cards);
        List<Card> listOne = new LinkedList<>();
        List<Card> listTwo = new LinkedList<>();
        List<Rank> kickers = new LinkedList<>();

        //We put the 3-size lists into new lists and the rest into a kicker list
        while(masterList.size() > 0){
            if(masterList.get(0).size() == 3 && listOne.size() == 0){
                listOne = masterList.get(0);
            }else if(masterList.get(0).size() == 3 && listTwo.size() == 0){
                listTwo = masterList.get(0);
            }else{
                if(masterList.get(0).size() != 0){
                    kickers.add(masterList.get(0).get(0).getRank());
                }
            }
            masterList.remove(0);
        }

        //If listOne doesn't have a size of 3, return null
        if(listOne.size() != 3){
            return null;
        }

        //There is a scenario where two out of the five lists have 3 cards in them
        //In this case, we return the higher rank one and make to lower rank one to a kicker
        List<Card> returnList = listOne;
        if(listTwo.size() == 3){
            if (rankToInt(listOne.get(0).getRank()) > rankToInt(listTwo.get(0).getRank())) {
                kickers.add(listTwo.get(0).getRank());
            } else {
                returnList = listTwo;
                kickers.add(listOne.get(0).getRank());
            }
        }

        //Now we convert the kickers list into an integer list based on the ranks
        int i;
        int[] intList = {};
        if(kickers.size() == 4){
            intList = new int[] {0,0,0,0};
        }
        if(kickers.size() == 2){
            intList = new int[] {0,0};
        }

        for(i=0;i<kickers.size();i++){
            intList[i] = rankToInt(kickers.get(i));
        }

        //We sort the kickers
        sortList(intList);

        return new PokerHand(4, returnList.get(0).getRank(), intToRank(intList[0]), intToRank(intList[1]), null, null, null, null);

    }

    private PokerHand isTwoPairs(List<Card> cards){

        List<List<Card>> masterList = makeRankLists(cards);

        //We make three lists and one more for the kickers
        //It can either be: 2 lists of length 2 and 3 kickers or 3 lists of length 2 and 1 kicker
        List<Card> listOne = new LinkedList<>();
        List<Card> listTwo = new LinkedList<>();
        List<Card> listThree = new LinkedList<>();
        List<Rank> kickers = new LinkedList<>();

        //Go through the master list and check for lists of length 2 / kickers
        int i;
        for(i=0;i<masterList.size();i++){
            if(masterList.get(i).size() == 2){
                if(listOne.size() == 0){
                    listOne = masterList.get(i);
                }else if(listTwo.size() == 0){
                    listTwo = masterList.get(i);
                }else{
                    listThree = masterList.get(i);
                }
            }
            if(masterList.get(i).size() == 1){
                kickers.add(masterList.get(i).get(0).getRank());
            }
        }

        //If either listOne or listTwo have sizes of under 2, return null
        if(listOne.size() != 2 || listTwo.size() != 2){
            return null;
        }

        //Make a returnListOne and returnListTwo
        //Then, make One the highest rank and Two the second highest rank
        //The lowest rank (if it exists) is converted to a kicker
        List<Card> returnListOne = new LinkedList<>();
        List<Card> returnListTwo = new LinkedList<>();

        if(rankToInt(listOne.get(0).getRank()) > rankToInt(listTwo.get(0).getRank())){
            returnListOne = listOne;
            returnListTwo = listTwo;
        }else{
            returnListOne = listTwo;
            returnListTwo = listOne;
        }
        if(listThree.size() != 0){
            if(rankToInt(listThree.get(0).getRank()) > rankToInt(returnListOne.get(0).getRank())){
                kickers.add(returnListTwo.get(0).getRank());
                returnListTwo = returnListOne;
                returnListOne = listThree;
            }
            if((rankToInt(listThree.get(0).getRank()) < rankToInt(returnListOne.get(0).getRank())) && (rankToInt(listThree.get(0).getRank()) > rankToInt(returnListTwo.get(0).getRank()))){
                kickers.add(returnListTwo.get(0).getRank());
                returnListTwo = listThree;
            }
            if(rankToInt(listThree.get(0).getRank()) < rankToInt(returnListTwo.get(0).getRank())){
                kickers.add(listThree.get(0).getRank());
            }
        }

        //Handle kickers
        int[] intList = {};
        if(kickers.size() == 3){
            intList = new int[] {0,0,0};
        }
        if(kickers.size() == 2){
            intList = new int[] {0,0};
        }

        for(i=0;i<kickers.size();i++){
            intList[i] = rankToInt(kickers.get(i));
        }

        //We sort the kickers
        sortList(intList);

        return new PokerHand(3, returnListOne.get(0).getRank(), returnListTwo.get(0).getRank(), intToRank(intList[0]), null, null, null, null);

    }

    private PokerHand isOnePair(List<Card> cards){

        List<List<Card>> masterList = makeRankLists(cards);
        List<Rank> pairs = new LinkedList<>();
        List<Rank> kickers = new LinkedList<>();

        //Fill pairs and kickers list
        int i;
        for(i=0;i<masterList.size();i++){
            if(masterList.get(i).size() == 2 && pairs.size() == 0){
                pairs.add(masterList.get(i).get(0).getRank());
            }
            if(masterList.get(i).size() == 1){
                kickers.add(masterList.get(i).get(0).getRank());
            }
        }

        //If pairs has a size of 0, return null
        if(pairs.size() == 0){
            return null;
        }

        //Handle kickers
        int[] intList = {0,0,0,0,0};
        for(i=0;i<kickers.size();i++){
            intList[i] = rankToInt(kickers.get(i));
        }

        //Sort kickers
        sortList(intList);

        return new PokerHand(2, pairs.get(0), intToRank(intList[0]), intToRank(intList[1]), intToRank(intList[2]), null, null, null);

    }

    private PokerHand isHighCard(List<Card> cards){

        int[] rankInts = {0,0,0,0,0,0,0};
        int i;

        for(i=0;i<cards.size();i++){
            rankInts[i] = rankToInt(cards.get(i).getRank());
        }

        rankInts = sortList(rankInts);

        return new PokerHand(1, intToRank(rankInts[0]), intToRank(rankInts[1]), intToRank(rankInts[2]), intToRank(rankInts[3]), intToRank(rankInts[4]), intToRank(rankInts[5]), intToRank(rankInts[6]));

    }


}


