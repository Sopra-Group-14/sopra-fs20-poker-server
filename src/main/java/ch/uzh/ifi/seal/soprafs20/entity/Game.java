package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.cards.WinnerCalculator;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The game class is responsible for the information regarding the state of a specific game.
 * It has its own instances of objects (e.g. chats, players).
 */
public class Game {

    //Define the objects
    //private final GameService gameService;
    private final PlayerChat playerChat = new PlayerChat();
    private final SpectatorChat spectatorChat = new SpectatorChat();
    private long gameId;
    private long gameHostID;
    private int maxPlayers;
    private String gameName;
    private String gameHostName;
    private String potType;
    private String hostToken;
    private GameRound gameRound;
    private int transactionNr;
    private Player smallBlind;
    private Player bigBlind;

    private Pot mainPot = new Pot();
    private Deck deck = new Deck();
    private WinnerCalculator winnerCalculator;

    //Create lists for spectators, players and the active players
    private List<Player> players = new LinkedList<>();
    private List<Spectator> spectators = new LinkedList<>();
    private List<Player> activePlayers = new LinkedList<>();
    private boolean roundOver;
    private boolean gameOver;

    //Create a list for the cards on the table
    private List<Card> tableCards = new LinkedList<>();
    private int timesRaisedPerPreflop = 0;
    private int timesRaisedPerFlop = 0;
    private int timesRaisedTurnCard= 0;
    private int timesRaisedRiverCard = 0;
    private List <Action> possibleActions = new ArrayList<>();
    private GameLog gameLog;
    //private List<GameLog> gameTracker = new LinkedList<>();

    public Game(String gameName) {
        this.gameLog = new GameLog();
        //this.gameLog=new GameLog(0,null,null,null,null,null,"TestName",0,null,1L,null,2L,0,0,false,false,0,true,false,null);
        gameLog.setGameName(gameName);
    }

    public Game(){}

    /*
Constructor
    public Game(String gameName, String hostName, String potType /*, int maxPlayers, GameService gameService* /){
        this.gameName = gameName;
        this.gameHostName = hostName;
        this.potType = potType;
        //this.maxPlayers = maxPlayers;
        /*this.gameService = gameService;
        this.id = assignId();* /
    }
*/

    /*private long assignId(){
        return gameService.getNextId();
    }*/

    public void setPotType(String potType) {
        this.potType = potType;
    }

    public String getPotType() {
        return potType;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameHostID(long gameHostID) {
        this.gameHostID = gameHostID;
    }

    public long getGameHostID() {
        return gameHostID;
    }

    public String getGameName(){return gameName;}

    public void setGameHostName(String gameHostName) {
        this.gameHostName = gameHostName;
    }

    public String getGameHostName(){
        return this.gameHostName;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void setBigBlind(Player bigBlind) {
        this.bigBlind = bigBlind;
    }

    public Player getBigBlind() {
        return bigBlind;
    }

    public void setSmallBlind(Player smallBlind) {
        this.smallBlind = smallBlind;
    }

    public Player getSmallBlind() {
        return smallBlind;
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }

    public List<Player> getPlayers(){return players;}
    public List<Player> getActivePlayers(){return activePlayers;}

    public int getTransactionNr(){return this.transactionNr;}
    public void setTransactionNr(int transactionNr){this.transactionNr = transactionNr;}

    public void addActivePlayer(Player player){
        this.activePlayers.add(player);
    }


    public Player getCurrentPlayer(long id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                return players.get(i);
            }
        }
        return null;
    }

    public Player getNextPlayer(Player currentPlayer) {
        if (activePlayers.get(activePlayers.size() - 1).getId() == currentPlayer.getId()) {
            return activePlayers.get(0);
        }
        else {
            for (int i = 0; i < activePlayers.size() - 1; i++) {
                if (activePlayers.get(i).getId() == currentPlayer.getId()) {
                    return activePlayers.get(i + 1);
                }
            }
            return null;
        }
    }


    public Player getPreviousPlayer(Player currentPlayer){
        if (activePlayers.get(0).getId() == currentPlayer.getId()) {
            return activePlayers.get(activePlayers.size()-1);
        }
        else {
            for (int i = 1; i < activePlayers.size(); i++) {
                if (activePlayers.get(i).getId() == currentPlayer.getId()) {
                    return activePlayers.get(i - 1);
                }

            }
            return null;
        }
    }

    public Player getPlayerById(long id){
        int i;
        for(i=0;i<players.size();i++){
            if(players.get(i).getId() == id){
                return players.get(i);
            }
        }
        return null;
    }

    public void addSpectator(Spectator spectator){
        this.spectators.add(spectator);
    }

    public void removeSpectator(Spectator spectator){
        this.spectators.remove(spectator);
    }

    public void setGameId(long id){this.gameId = id;}

    public long getGameId(){return gameId;}

    public void setHostToken(String hostToken) {
        this.hostToken = hostToken;
    }

    public String getHostToken(){
        return this.hostToken;
    }

    public void playerFolds(Player player){
        player.fold();
        activePlayers.remove(player);
    }

    public void setGameOver(){this.gameOver = true;}
    public boolean isGameOver(){return this.gameOver;}
    public void setRoundOver(){this.roundOver = true;}
    public boolean isRoundOver(){return this.roundOver;}

    public int getMaxPlayers(){return maxPlayers;}

    public void updateBlinds(){}


    public GameRound getGameRound() {
        return gameRound;
    }

    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public List<Card> getTableCards(){
        return new LinkedList<>(tableCards);
    }

    public void addTableCard(){
        if (tableCards.size()>2){
            throw new SopraServiceException("There are already three cards on the table in the game");
        }
        deck.shuffle();
        tableCards.add(deck.getTopCard());
    }

    public void removeTableCards(){
        tableCards = new LinkedList<>();
    }


    public GameLog nullGameLog(){

        this.possibleActions.add(Action.BET);

        setGameLog(new GameLog(-1, GameRound.Preflop, Action.NONE, this.getPlayers(), this.getActivePlayers(), this.getTableCards(),
                this.getGameName(), 0, "", (long) -1, "", (long) -1, 0, 0, false,
                false, 0, false, false, this.getPossibleActions()));


        return getGameLog();
    }

//    public void removeGameLog(){
//        this.gameTracker = new ArrayList<>();
//    }
//
//    public void addGameLog(GameLog gameLog){
//        gameTracker.add(gameLog);
//    }
//
//
//    public GameLog getGameLog(){
//        return this.gameTracker.get(gameTracker.size());
//    }


    public int getTimesRaisedPerPreflop() {
        return timesRaisedPerPreflop;
    }

    public void setTimesRaisedPerPreflop(int timesRaisedPerPreflop) {
        this.timesRaisedPerPreflop = timesRaisedPerPreflop;
    }

    public int getTimesRaisedTurnCard() {
        return timesRaisedTurnCard;
    }

    public void setTimesRaisedTurnCard(int timesRaisedTurnCard) {
        this.timesRaisedTurnCard = timesRaisedTurnCard;
    }

    public int getTimesRaisedPerFlop() {
        return timesRaisedPerFlop;
    }

    public void setTimesRaisedPerFlop(int timesRaisedPerFlop) {
        this.timesRaisedPerFlop = timesRaisedPerFlop;
    }

    public int getTimesRaisedRiverCard() {
        return timesRaisedRiverCard;
    }

    public void setTimesRaisedRiverCard(int timesRaisedRiverCard) {
        this.timesRaisedRiverCard = timesRaisedRiverCard;
    }

    public List<Action> getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(List<Action> possibleActions) {
        this.possibleActions = possibleActions;
    }


    public GameLog getGameLog(){

        //TESTING PURPOSES
        return this.gameLog; }

    public void setGameLog(GameLog gameLog) {
        this.gameLog = gameLog;
    }
}



