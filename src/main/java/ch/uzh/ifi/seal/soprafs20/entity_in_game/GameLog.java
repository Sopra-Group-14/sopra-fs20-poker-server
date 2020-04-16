package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;

import java.util.List;

/**
 * This object is responsible for documenting what happens in a game.
 */
public class GameLog {

    private int transactionNr, raiseAmount, playerPot, amountToCall, potAmount;
    private GameRound gameRound;
    private String playerName, nextPlayerName;
    private List<Player> players;
    private List<Player> activePlayers;
    private List<Card> revealedCards;
    private String gameName;
    private long playerId, nextPlayerId;
    private boolean roundOver, gameOver;
    private Action action;

    //All the parameters are set in the constructor
    public GameLog(int transactionNr, GameRound gameRound, Action action, List<Player> players, List<Player> activePlayers, List<Card> revealedCards, String gameName, int raiseAmount, String playerName, long playerId, String nextPlayerName, long nextPlayerId, int playerPot, int potAmount, boolean roundOver, boolean gameOver, int amountToCall){
        this.transactionNr = transactionNr;
        this.gameRound = gameRound;
        this.action = action;
        this.players = players;
        this.activePlayers = activePlayers;
        this.revealedCards = revealedCards;
        this.gameName = gameName;
        this.raiseAmount = raiseAmount;
        this.playerName = playerName;
        this.playerId = playerId;
        this.nextPlayerName = nextPlayerName;
        this.nextPlayerId = nextPlayerId;
        this.playerPot = playerPot;
        this.potAmount = potAmount;
        this.roundOver = roundOver;
        this.gameOver = gameOver;
        this.amountToCall = amountToCall;
    }


    public int getTransactionNr(){return transactionNr;}

    public GameRound getGameRound(){return gameRound;}

    public Action getAction(){return action;}

    public int getRaiseAmount(){return raiseAmount;}

    public String getPlayerName(){return playerName;}

    public long getPlayerId(){return playerId;}

    public String getNextPlayerName(){return nextPlayerName;}

    public long getNextPlayerId(){return nextPlayerId;}

    public int getPlayerPot(){return playerPot;}

    public boolean getRoundOver(){return roundOver;}

    public boolean getGameOver(){return gameOver;}

    public int getAmountToCall(){return amountToCall;}

    public void setAmountToCall(int amount){amountToCall = amount;}

}
