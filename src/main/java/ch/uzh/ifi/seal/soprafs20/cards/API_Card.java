package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;

public class API_Card {
    private String apiCard;

    public static String concat(String s1, String s2){
        return s1 + s2;
    }

    public API_Card(Suit suit, Rank rank){
        String s1, s2;

        switch (suit){
            case SPADES:
                s2 = "s";
                break;
            case CLUBS:
                s2 = "c";
                break;
            case DIAMONDS:
                s2 = "d";
                break;
            case HEARTS:
                s2 = "h";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + suit);
        }

        switch (rank){
            case TWO:
                s1 = "2";
                break;
            case THREE:
                s1 = "3";
                break;
            case FOUR:
                s1 = "4";
                break;
            case FIVE:
                s1 = "5";
                break;
            case SIX:
                s1 = "6";
                break;
            case SEVEN:
                s1 = "7";
                break;
            case EIGHT:
                s1 = "8";
                break;
            case NINE:
                s1 = "9";
                break;
            case TEN:
                s1 = "T";
                break;
            case JACK:
                s1 = "J";
                break;
            case KING:
                s1 = "K";
                break;
            case QUEEN:
                s1 = "Q";
                break;
            case ACE:
                s1 = "A";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rank);
        }

        this.apiCard = concat(s1, s2);
    }


    public String getApiCard() {
        return apiCard;
    }

    public void setApiCard(String myCard) {
        this.apiCard = myCard;
    }
}
