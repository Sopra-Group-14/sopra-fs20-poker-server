package ch.uzh.ifi.seal.soprafs20.entity_in_game;

/**
 * Representation of a Pot; there can be a main and a side Pot
 */
public class Pot {

    private int currentAmount = 0;

    public void addAmount(int amount){
        currentAmount += amount;
    }

    public void removeAmount(int amount){
        currentAmount -= amount;
    }

    public int getAmount(){
        return currentAmount;
    }

}
