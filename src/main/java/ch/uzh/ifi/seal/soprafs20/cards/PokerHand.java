package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;

import java.util.LinkedList;
import java.util.List;

public class PokerHand {

    private Rank firstAttribute, secondAttribute, thirdAttribute, fourthAttribute, fifthAttribute, sixthAttribute, seventhAttribute;
    private int comboValue;


    public PokerHand(int comboValue, Rank firstAttribute, Rank secondAttribute, Rank thirdAttribute, Rank fourthAttribute, Rank fifthAttribute, Rank sixthAttribute, Rank seventhAttribute){
        this.firstAttribute = firstAttribute;
        this.secondAttribute = secondAttribute;
        this.thirdAttribute = thirdAttribute;
        this.fourthAttribute = fourthAttribute;
        this.fifthAttribute = fifthAttribute;
        this.sixthAttribute = sixthAttribute;
        this.seventhAttribute = seventhAttribute;
        this.comboValue = comboValue;
    }

    public List<Rank> getRankList(){
        List<Rank> rankList = new LinkedList<>();
        rankList.add(firstAttribute);
        rankList.add(secondAttribute);
        rankList.add(thirdAttribute);
        rankList.add(fourthAttribute);
        rankList.add(fifthAttribute);
        rankList.add(sixthAttribute);
        rankList.add(seventhAttribute);

        return rankList;

    }

    public int getComboValue(){
        return comboValue;
    }

}

