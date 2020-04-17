package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;

import java.util.LinkedList;
import java.util.List;

public class PokerHand {

    private Rank highestInCombo, secondHighestInCombo, highestKicker, secondHighestKicker, thirdHighestKicker;
    private int comboValue;


    public PokerHand(int comboValue, Rank highestInCombo, Rank secondHighestInCombo, Rank highestKicker, Rank secondHighestKicker, Rank thirdHighestKicker){
        this.highestInCombo = highestInCombo;
        this.secondHighestInCombo = secondHighestInCombo;
        this.highestKicker = highestKicker;
        this.secondHighestKicker = secondHighestKicker;
        this.thirdHighestKicker = thirdHighestKicker;
        this.comboValue = comboValue;
    }

    public List<Rank> getRankList(){
        List<Rank> rankList = new LinkedList<>();
        rankList.add(highestInCombo);
        rankList.add(secondHighestInCombo);
        rankList.add(highestKicker);
        rankList.add(secondHighestKicker);
        rankList.add(thirdHighestKicker);

        return rankList;

    }

    public int getComboValue(){
        return comboValue;
    }

}
