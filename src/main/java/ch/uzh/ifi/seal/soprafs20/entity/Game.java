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

    private Pot pot = new Pot();
    private Deck deck = new Deck();
    private WinnerCalculator winnerCalculator = new WinnerCalculator();

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
    private int roundCounter;
    private int actionsAfterRaise;
    private int checksPerRound;


    //private List<GameLog> gameTracker = new LinkedList<>();

    public Game(String gameName) {
        this.gameLog = new GameLog();
        //this.gameLog=new GameLog(0,null,null,null,null,null,"TestName",0,null,1L,null,2L,0,0,false,false,0,true,false,null);
        gameLog.setGameName(gameName);
        gameLog.setGameStarted(false);
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
        gameLog.setPlayers(players);

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
        removeActivePlayer(player);
        gameLog.setPlayers(players);
    }

    public List<Player> getPlayers(){return players;}
    public List<Player> getActivePlayers(){return activePlayers;}

    public int getTransactionNr(){return this.transactionNr;}
    public void setTransactionNr(int transactionNr){this.transactionNr = transactionNr;}

    public void addActivePlayer(Player player){
        this.activePlayers.add(player);
        gameLog.setActivePlayers(activePlayers);
    }

    public void removeActivePlayer(Player player){
        this.activePlayers.remove(player);
        gameLog.setActivePlayers(activePlayers);
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

    public void setGameOver(){
        this.setGameOver(true);}
    public boolean isGameOver(){return this.gameOver;}
    public void setRoundOver(){
        this.setRoundOver(true);}
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
        if (tableCards.size()>4){
            throw new SopraServiceException("There are already five cards on the table in the game");
        }
        //deck.shuffle();
        tableCards.add(deck.getTopCard());
        gameLog.setRevealedCards(tableCards);
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

    public void startGame(){

        //hand out hand cards before round one
        deck.shuffle();
        int i,e;
        for(i=0;i<players.size();i++){
            for(e=0;e<2;e++){
                players.get(i).addToHand(deck.getTopCard());
            }
        }
        gameLog.setActivePlayers(activePlayers);
        gameLog.setPlayers(players);
        List<Action> actionList = new LinkedList<>();
        actionList.add(Action.BET);
        actionList.add(Action.FOLD);
        gameLog.setPossibleActions(actionList);
        gameLog.setGameStarted(true);
        gameLog.setGameOver(false);
        setGameOver(false);
        roundCounter = 0;
        setActionsAfterRaise(0);
        setChecksPerRound(0);
        gameLog.setGameRound(GameRound.Preflop);
        setGameRound(GameRound.Preflop);
        gameLog.setNextPlayersTurn(false);
        gameLog.setThisPlayersTurn(true);
        gameLog.setPlayerName(activePlayers.get(0).getPlayerName());
        gameLog.setPlayerId(activePlayers.get(0).getId());
        gameLog.setNextPlayerId(activePlayers.get(0).getId());
        gameLog.setNextPlayerName(activePlayers.get(0).getPlayerName());
        gameLog.setRevealedCards(tableCards);

    }

    public void startNewRound(){
        //enter folded players back into the list active players  at same position and make them unfolded
        activePlayers.clear();
        for (int i = 0; i< players.size(); i++){
            activePlayers.add(i, players.get(i));
            activePlayers.get(i).setFolded(false);
        }

        gameLog.setPotAmount(0);
        gameLog.setPlayerPot(0);

//shift active players and players to the right so that BigBlind and SmallBlind is shifted one player
        Player player = players.get(players.size()-1);
        Player activePlayer = activePlayers.get(players.size()-1);

        for (int i = players.size()-1; i>0; i--){
            players.set(i, players.get(i-1));
            activePlayers.set(i, activePlayers.get(i-1));
        }
        players.set(0, player);
        activePlayers.set(0, activePlayer);

        gameLog.setBigBlind(activePlayers.get(1));
        setBigBlind(activePlayers.get(1));
        gameLog.setSmallBlind(activePlayers.get(0));
        setSmallBlind(activePlayers.get(0));

        // Important: setThisPlayerTurn for all players but nextPlayer to false
        for (Player player1 : activePlayers) {
            player1.setThisPlayersTurn(false);
        }
        activePlayers.get(0).setThisPlayersTurn(true);

        //Delete all table Cards
        tableCards.clear();
        gameLog.setRevealedCards(tableCards);


        //hand out hand cards before new round
        for (int i=0;i<players.size();i++){
            players.get(i).getHand().clear();
        }
        Deck newDeck = new Deck();
        newDeck.shuffle();
        int i,e;
        for(i=0;i<players.size();i++){
            for(e=0;e<2;e++){
                players.get(i).addToHand(newDeck.getTopCard());
            }
        }
        gameLog.setActivePlayers(activePlayers);
        gameLog.setPlayers(players);


        List<Action> actionList = new LinkedList<>();
        actionList.clear();
        actionList.add(Action.BET);
        actionList.add(Action.FOLD);
        gameLog.setPossibleActions(actionList);
        gameLog.setGameStarted(true);
        gameLog.setGameOver(false);
        roundCounter = 0;
        setActionsAfterRaise(0);
        setChecksPerRound(0);
        gameLog.setGameRound(GameRound.Preflop);
        setGameRound(GameRound.Preflop);
        gameLog.setNextPlayersTurn(false);
        gameLog.setThisPlayersTurn(true);
        gameLog.setPlayerName(activePlayers.get(0).getPlayerName());
        gameLog.setPlayerId(activePlayers.get(0).getId());
        gameLog.setNextPlayerId(activePlayers.get(0).getId());
        gameLog.setNextPlayerName(activePlayers.get(0).getPlayerName());

    }

    public void playGame(Action action, long playerId) {

        List<Player> winners;

        //if less than one player in the game than gameOver
        if (players.size() <= 1 ){
            gameLog.setGameOver(true);
            setGameOver(true);
            return;
        }
        //if only one player in the game has credit, than game is over
        int playersWithCredit = 0;
        for (int i = 0;i < players.size();i++){
            if (players.get(i).getCredit()>0){
                playersWithCredit++;
            }
        }
        if (playersWithCredit <2){
            gameLog.setGameOver(true);
            setGameOver(true);
        }

        if (gameOver == true){
            //calculate the winners
            winners = winnerCalculator.isWinner(players, tableCards);
            gameLog.setWinners(winners);
            //calculate the amount won by every winner
            int wonAmount = pot.getAmount()/winners.size();
            gameLog.setWonAmount(wonAmount);
            //add won amount to the credit of the winnerPlayers
            for (int i =0; i< winners.size(); i++){
                winners.get(i).addCredit(wonAmount);
            }
            gameLog.setPlayers(players);
        }

        //set roundOver to false
        gameLog.setRoundOver(false);
        setRoundOver(false);

        if (action == Action.RAISE || action == Action.BET){
            setActionsAfterRaise(0);
        }else if (action == Action.FOLD) {
            setActionsAfterRaise(getActionsAfterRaise() + 0);
        }
        else if (action == Action.CALL){
                setActionsAfterRaise(getActionsAfterRaise() + 1);
        }
        else if (action == Action.CHECK){
            setChecksPerRound(getChecksPerRound()+1);
        }

        //old round finished and initialising new round
        if (getActionsAfterRaise() == activePlayers.size()-1 || getChecksPerRound() == activePlayers.size()){
            setActionsAfterRaise(0);
            setChecksPerRound(0);
            roundCounter +=1;



           //uncover a card before the second, third and fourth rounds
            // name second round Flop, third round TurnCard and fourth round RiverCard
            if (roundCounter ==1){
                addTableCard();
                addTableCard();
                addTableCard();
                gameLog.setGameRound(GameRound.Flop);
                setGameRound(GameRound.Flop);
            }else if (roundCounter ==2){
                addTableCard();
                gameLog.setGameRound(GameRound.TurnCard);
                setGameRound(GameRound.TurnCard);
            }else if (roundCounter == 3){
                addTableCard();
                gameLog.setGameRound(GameRound.RiverCard);
                setGameRound(GameRound.RiverCard);
            }else if (roundCounter >= 4){

                //all rounds are finished
                possibleActions.clear();
                gameLog.setPossibleActions(possibleActions);
                setPossibleActions(possibleActions);
                gameLog.setRoundOver(true);
                setRoundOver(true);


                //calculate the winners
                winners = winnerCalculator.isWinner(players, tableCards);
                gameLog.setWinners(winners);
                //calculate the amount won by every winner
                int wonAmount = pot.getAmount()/winners.size();
                gameLog.setWonAmount(wonAmount);
                //add won amount to the credit of the winnerPlayers
                for (int i =0; i< winners.size(); i++){
                    winners.get(i).addCredit(wonAmount);
                }
                gameLog.setPlayers(players);
                pot.removeAmount(pot.getAmount());
                gameLog.setPotAmount(0);


                //open new round again
                startNewRound();
            }

        }



    }


    public Pot getPot() {
        return pot;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    public int getActionsAfterRaise() {
        return actionsAfterRaise;
    }

    public void setActionsAfterRaise(int actionsAfterRaise) {
        this.actionsAfterRaise = actionsAfterRaise;
    }

    public int getChecksPerRound() {
        return checksPerRound;
    }

    public void setChecksPerRound(int checksPerRound) {
        this.checksPerRound = checksPerRound;
    }

    public void setRoundOver(boolean roundOver) {
        this.roundOver = roundOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setTableCards(List<Card> tableCards) {
        this.tableCards = tableCards;
    }
}



