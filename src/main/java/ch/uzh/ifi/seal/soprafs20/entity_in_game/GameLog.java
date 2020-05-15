package ch.uzh.ifi.seal.soprafs20.entity_in_game;

import ch.uzh.ifi.seal.soprafs20.cards.API_Card;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This object is responsible for documenting what happens in a game.
 */
public class GameLog {

    private int transactionNr;
    private int raiseAmount;
    private int playerPot;
    private int amountToCall;
    private int potAmount;
    private GameRound gameRound;
    private String playerName;
    private String nextPlayerName;
    private List<Player> players;
    private List<Player> activePlayers;
    private List<Player> winners;
    private int wonAmount;
    private List<Card> revealedCards;
    private List<String> revealedAPICards;
    private List<Action> possibleActions;
    private String gameName;
    private long playerId;
    private long nextPlayerId;
    private boolean roundOver;
    private boolean gameOver;
    private boolean thisPlayersTurn;
    private boolean nextPlayersTurn;
    private Action action;
    private List<Card> tableCards = new LinkedList<>();
    private boolean gameStarted;
    private Player bigBlind;
    private Player smallBlind;
    API_Card api_card;

    //All the parameters are set in the constructor


    public GameLog(int transactionNr, GameRound gameRound, Action action, List<Player> players, List<Player> activePlayers, List<Card> revealedCards, String gameName,
					int raiseAmount, String playerName, long playerId, String nextPlayerName, long nextPlayerId, int playerPot, int potAmount, boolean roundOver,
					boolean gameOver, int amountToCall, boolean thisPlayersTurn, boolean nextPlayersTurn, List<Action> possibleActions){

        this.setTransactionNr(transactionNr);
        this.setGameRound(gameRound);
        this.setAction(action);
        this.setPlayers(players);
        this.setActivePlayers(activePlayers);
        this.setRevealedCards(revealedCards);
        this.setRevealedAPICards(revealedCards);
        this.setGameName(gameName);
        this.setRaiseAmount(raiseAmount);
        this.setPlayerName(playerName);
        this.setPlayerId(playerId);
        this.setNextPlayerName(nextPlayerName);
        this.setNextPlayerId(nextPlayerId);
        this.setPlayerPot(playerPot);
        this.setPotAmount(potAmount);
        this.setRoundOver(roundOver);
        this.setGameOver(gameOver);
        this.setAmountToCall(amountToCall);
        this.setThisPlayersTurn(thisPlayersTurn);
        this.setNextPlayersTurn(nextPlayersTurn);
        this.setPossibleActions(possibleActions);


    }

    public GameLog() {

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

    public boolean getRoundOver(){return isRoundOver();}

    public boolean getGameOver(){return isGameOver();}

    public int getAmountToCall(){return amountToCall;}

    public boolean getThisPlayersTurn(){return isThisPlayersTurn();}

    public boolean getNextPlayersTurn(){return isNextPlayersTurn();}

    public void setAmountToCall(int amount){amountToCall = amount;}

    public List<Action> getPossibleActions(){return possibleActions;}

    public String getGameName(){return gameName;}

    public void setGameName(String name){this.gameName = name;}

    public void setTransactionNr(int transactionNr){ this.transactionNr = transactionNr;}

    public void setGameRound(GameRound gameRound){this.gameRound = gameRound;}

    public void setAction(Action action){this.action = action;}

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<Player> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public List<Card> getRevealedCards() {
        return revealedCards;
    }

    public void setRevealedCards(List<Card> revealedCards) {
        this.revealedCards = revealedCards;
        setRevealedAPICards(this.revealedCards);
    }

    public void setRaiseAmount(int raiseAmount) {
        this.raiseAmount = raiseAmount;
    }

    public void setPlayerPot(int playerPot) {
        this.playerPot = playerPot;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setNextPlayerName(String nextPlayerName) {
        this.nextPlayerName = nextPlayerName;
    }

    public void setPossibleActions(List<Action> possibleActions) {
        this.possibleActions = possibleActions;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setNextPlayerId(long nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }

    public boolean isRoundOver() {
        return roundOver;
    }

    public void setRoundOver(boolean roundOver) {
        this.roundOver = roundOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isThisPlayersTurn() {
        return thisPlayersTurn;
    }

    public void setThisPlayersTurn(boolean thisPlayersTurn) {
        this.thisPlayersTurn = thisPlayersTurn;
    }

    public boolean isNextPlayersTurn() {
        return nextPlayersTurn;
    }

    public void setNextPlayersTurn(boolean nextPlayersTurn) {
        this.nextPlayersTurn = nextPlayersTurn;
    }

    public int getPotAmount() {
        return potAmount;
    }

    public void setPotAmount(int potAmount) {
        this.potAmount = potAmount;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public void setWinners(List<Player> winners) {
        this.winners = winners;
    }

    public int getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(int wonAmount) {
        this.wonAmount = wonAmount;
    }

    public Player getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(Player bigBlind) {
        this.bigBlind = bigBlind;
    }

    public Player getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(Player smallBlind) {
        this.smallBlind = smallBlind;
    }

    public List<String> getRevealedAPICards() {
        return revealedAPICards;
    }

    public void setRevealedAPICards(List<Card> revealedCards) {
        if(api_card != null) {
            this.revealedAPICards.clear();
        }else{
            this.revealedAPICards = new ArrayList<>();
        }
        for (int i = 0; i<revealedCards.size(); i++) {
            api_card = new API_Card(revealedCards.get(i).getSuit(), revealedCards.get(i).getRank());
            this.revealedAPICards.add(api_card.getApiCard());
        }
    }
}
