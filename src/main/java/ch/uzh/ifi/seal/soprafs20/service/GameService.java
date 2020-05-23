package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.WinnerCalculator;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;
import ch.uzh.ifi.seal.soprafs20.controller.GameController;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Pot;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Spectator;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The GameService takes care of what happens with Games.
 */
@Service
@Transactional
public class GameService {

    private final UserService userService;
    //private final Logger gameLog = LoggerFactory.getLogger(GameService.class);
    private final Logger log = LoggerFactory.getLogger(GameController.class);
    //The GameSelect is essentially just a list that holds all games. The games can then be accessed through it.
    private final GameSelect gameSelect = new GameSelect();

    private int currentId = 0;
    private String gameName;
    private long hostID;
    private String potType;
    public GameService(UserService userService){
        this.userService = userService;
    }

    private Player getPlayerWithMostAmountInPot(Game game){
        Player playerWithMostAmountInPot;
        playerWithMostAmountInPot = game.getActivePlayers().get(0);
        for (int i = 0; i<game.getActivePlayers().size();i++){
            if (playerWithMostAmountInPot.getAmountInPot() <= game.getActivePlayers().get(i).getAmountInPot()){
                playerWithMostAmountInPot = game.getActivePlayers().get(i);
            }
        }
        return playerWithMostAmountInPot;
    }


    public Game createGame(String gameName, long hostID, String potType){

        /*createGame receives gameName, hostId and potType from client. The game constructor expects the hostName;

         */

        //TODO resolve null pointer exception if user that does not exist creates a game
        //--> To avoid this, set the hostID to -1; it will work for testing purposes.
        String hostName, hostToken;

        if(hostID > -1){
            User host = userService.getUserById(hostID);
            hostName = host.getUsername();
            hostToken = host.getToken();
        }else{
            hostName = "N/A";
            hostToken = "N/A";
        }

        Game newGame = new Game(gameName);

        long currentId;
        if(this.gameSelect.getAllGames() != null){
            currentId = this.gameSelect.getAllGames().size() + 1;
        }else{
            currentId = 1;
        }

        newGame.setGameId(currentId);
        newGame.setGameName(gameName);
        newGame.setPotType(potType);
        newGame.setGameHostID(hostID);
        newGame.setGameHostName(hostName);
        newGame.setHostToken(hostToken);

//        GameLog nullGameLog = newGame.nullGameLog();

        gameSelect.addGame(newGame);
//        newGame.addGameLog(nullGameLog);

        return newGame;

    }

    public Game getGame(long gameId){
        return gameSelect.getGameById(gameId);
    }

    public void addHost(long hostID, Game game){

        User playerHost = userService.getUserById(hostID);
        Player player = userService.setToPlayer(playerHost);

        player.toggleReadyStatus();
        game.addPlayer(player);
        game.addActivePlayer(player);

    }

    public void addJoiningPlayer(long userId, long gameId){

        User joiningUser = userService.getUserById(userId);
        Player joiningPlayer = new Player(joiningUser);
        Game game = gameSelect.getGameById(gameId);
        game.addPlayer(joiningPlayer);
        game.addActivePlayer(joiningPlayer);
        game.getGameLog().setBigBlind(game.getActivePlayers().get(1));
        game.getGameLog().setSmallBlind(game.getActivePlayers().get(0));
    }

    public void addSpectator(Long gameId){

        Spectator spectator = new Spectator();
        Game game = gameSelect.getGameById(gameId);
        Long id = game.getNextSpectatorId();
        spectator.setId(id);
        game.addSpectator(spectator);

    }

    public void togglePlayerReadyStatus(long gameid, long playerid){

        gameSelect.getGameById(gameid).getPlayerById(playerid).toggleReadyStatus();

    }

    public GameLog getGameLog(long gameId){

        return gameSelect.getGameById(gameId).getGameLog();

    }

    public void addGame(Game game){

        gameSelect.addGame(game);

    }

    public GameLog executeAction(Action action, int amount, long gameId, long playerId){


        Game game = gameSelect.getGameById(gameId);
        GameLog gameLog = game.getGameLog();
        List<Player> players = game.getPlayers();
        List<Player> activePlayers = game.getActivePlayers();
        Player currentPlayer = game.getCurrentPlayer(playerId);
        Player previousPlayer = game.getPreviousPlayer(currentPlayer);
        Player nextPlayer = game.getNextPlayer(currentPlayer);
        Pot pot = game.getPot();
        int smallBlind = 5;
        int bigBlind = 10;



        //The player tries to take an action when it is not their turn
        if (!activePlayers.contains(currentPlayer) ||  !currentPlayer.isThisPlayersTurn()){
            log.info("player tries to take action, when not his turn");
            String baseErrorMessage = "Not Player %s turn!";
            throw new SopraServiceException(String.format(baseErrorMessage, currentPlayer.getPlayerName()));
        }

        //The player ID does not exist
        if (!players.contains(currentPlayer)){
            String baseErrorMessage = "Player %s with Id %d does not exist in the game with id %d !";
            throw new SopraServiceException(String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId(), game.getGameId()));
        }

        //The user has already folded
        if (!activePlayers.contains(currentPlayer) || currentPlayer.hasFolded()){
            String baseErrorMessage = "The Player %s with Id %d has already folded and does not participate in the current round anymore!";
            throw new SopraServiceException(String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
        }


        //the first player of first round (preflop) has to bet the small blind
        if (game.getGameRound() == GameRound.Preflop && pot.getAmount() == 0 && currentPlayer == activePlayers.get(0)){
            if (action != Action.BET || amount != smallBlind){
                String baseErrorMessage = "The Player %s with Id %d has to bet the small blind %d because he is the first player of first round!";
                throw new SopraServiceException(String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId(), smallBlind));
            }
        }

        //the second player of first round has to raise (preflop)  at least as high as the bigblind or higher
        if (game.getGameRound() == GameRound.Preflop && pot.getAmount() == smallBlind && currentPlayer == activePlayers.get(1)){
            if (action != Action.RAISE || amount != smallBlind){
                String baseErrorMessage = "The Player %s with Id %d has to raise by the amount of the small blind %d because he is the second player of first round!";
                throw new SopraServiceException(String.format(baseErrorMessage, baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId(), smallBlind ));
            }
        }



        if (action == Action.BET){

            currentPlayer.removeCredit(amount);
            currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
            pot.addAmount(amount);
            game.setTransactionNr(game.getTransactionNr() + 1);

            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (int i = 0; i < activePlayers.size(); i++){
                activePlayers.get(i).setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);


            String a= Integer.toString(game.getTransactionNr());
            gameLog.setTransactionNr(game.getTransactionNr());
            gameLog.setGameRound(game.getGameRound());
            gameLog.setAction(Action.BET);
            gameLog.setPlayers(game.getPlayers());
            gameLog.setActivePlayers(game.getActivePlayers());
            gameLog.setRevealedCards(game.getTableCards());
            gameLog.setGameName(game.getGameName());
            gameLog.setRaiseAmount(amount);
            gameLog.setPlayerName(currentPlayer.getPlayerName());
            gameLog.setPlayerId(currentPlayer.getId());
            gameLog.setNextPlayerName(nextPlayer.getPlayerName());
            gameLog.setNextPlayerId(nextPlayer.getId());
            gameLog.setPlayerPot(currentPlayer.getAmountInPot());
            gameLog.setPotAmount(pot.getAmount());
            gameLog.setRoundOver(game.isRoundOver());
            gameLog.setGameOver(game.isGameOver());
            gameLog.setAmountToCall(Math.min(amount, nextPlayer.getCredit()));
            gameLog.setThisPlayersTurn(currentPlayer.isThisPlayersTurn());
            gameLog.setNextPlayersTurn(nextPlayer.isThisPlayersTurn());

            //return gameLog to GameController


        }

        if(action == Action.FOLD){
            game.playerFolds(currentPlayer);

            if (activePlayers.size()<2){
                game.setRoundOver();
                gameLog.setRoundOver(true);
                activePlayers.get(0).addCredit(game.getPot().getAmount());
                gameLog.setActivePlayers(activePlayers);
                game.getPot().removeAmount(game.getPot().getAmount());
                gameLog.setPotAmount(game.getPot().getAmount());
                game.startNewRound();
            }else{
                for (int i = 0; i < activePlayers.size(); i++){
                    activePlayers.get(i).setThisPlayersTurn(false);
                }
                nextPlayer.setThisPlayersTurn(true);

                gameLog.setNextPlayerName(nextPlayer.getPlayerName());
                gameLog.setNextPlayerId(nextPlayer.getId());
            }

            gameLog.setTransactionNr(game.getTransactionNr());
            gameLog.setGameRound(game.getGameRound());
            gameLog.setAction(Action.FOLD);
            gameLog.setPlayers(game.getPlayers());
            gameLog.setActivePlayers(game.getActivePlayers());
            gameLog.setRevealedCards(game.getTableCards());
            gameLog.setGameName(game.getGameName());
            gameLog.setRaiseAmount(0);
            gameLog.setPlayerName(currentPlayer.getPlayerName());
            gameLog.setPlayerId(currentPlayer.getId());
            gameLog.setPlayerPot(currentPlayer.getAmountInPot());
            gameLog.setPotAmount(pot.getAmount());
            gameLog.setRoundOver(game.isRoundOver());
            gameLog.setGameOver(game.isGameOver());
            gameLog.setAmountToCall(Math.min(getPlayerWithMostAmountInPot(game).getAmountInPot()-nextPlayer.getAmountInPot(), nextPlayer.getCredit()));
            gameLog.setThisPlayersTurn(currentPlayer.isThisPlayersTurn());
            gameLog.setNextPlayersTurn(nextPlayer.isThisPlayersTurn());


        }
        if(action == Action.RAISE) {
            int addedAmount = (getPlayerWithMostAmountInPot(game).getAmountInPot() - currentPlayer.getAmountInPot()) + amount;

            if (amount<=1){
                String baseErrorMessage = "The Player %s tries to raise by an amount lower than 1!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }


                currentPlayer.removeCredit(addedAmount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + addedAmount);
                pot.addAmount(addedAmount);
                game.setTransactionNr(game.getTransactionNr() + 1);
            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);


            gameLog.setTransactionNr(game.getTransactionNr());
            gameLog.setGameRound(game.getGameRound());
            gameLog.setAction(Action.RAISE);
            gameLog.setPlayers(game.getPlayers());
            gameLog.setActivePlayers(game.getActivePlayers());
            gameLog.setRevealedCards(game.getTableCards());
            gameLog.setGameName(game.getGameName());
            gameLog.setRaiseAmount(amount);
            gameLog.setPlayerName(currentPlayer.getPlayerName());
            gameLog.setPlayerId(currentPlayer.getId());
            gameLog.setNextPlayerName(nextPlayer.getPlayerName());
            gameLog.setNextPlayerId(nextPlayer.getId());
            gameLog.setPlayerPot(currentPlayer.getAmountInPot());
            gameLog.setPotAmount(pot.getAmount());
            gameLog.setRoundOver(game.isRoundOver());
            gameLog.setGameOver(game.isGameOver());
            gameLog.setAmountToCall(Math.min(getPlayerWithMostAmountInPot(game).getAmountInPot()-nextPlayer.getAmountInPot(), nextPlayer.getCredit()));
            gameLog.setThisPlayersTurn(currentPlayer.isThisPlayersTurn());
            gameLog.setNextPlayersTurn(nextPlayer.isThisPlayersTurn());

        }

        if(action == Action.CALL) {
            int amountToCall = Math.min(currentPlayer.getCredit(), getPlayerWithMostAmountInPot(game).getAmountInPot() - currentPlayer.getAmountInPot());
                currentPlayer.removeCredit(amountToCall);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amountToCall);
                pot.addAmount(amountToCall);
                game.setTransactionNr(game.getTransactionNr() + 1);


            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);

            gameLog.setTransactionNr(game.getTransactionNr());
            gameLog.setGameRound(game.getGameRound());
            gameLog.setAction(Action.CALL);
            gameLog.setPlayers(game.getPlayers());
            gameLog.setActivePlayers(game.getActivePlayers());
            gameLog.setRevealedCards(game.getTableCards());
            gameLog.setGameName(game.getGameName());
            gameLog.setRaiseAmount(0);
            gameLog.setPlayerName(currentPlayer.getPlayerName());
            gameLog.setPlayerId(currentPlayer.getId());
            gameLog.setNextPlayerName(nextPlayer.getPlayerName());
            gameLog.setNextPlayerId(nextPlayer.getId());
            gameLog.setPlayerPot(currentPlayer.getAmountInPot());
            gameLog.setPotAmount(pot.getAmount());
            gameLog.setRoundOver(game.isRoundOver());
            gameLog.setGameOver(game.isGameOver());
            gameLog.setAmountToCall(Math.min(getPlayerWithMostAmountInPot(game).getAmountInPot()-nextPlayer.getAmountInPot(), nextPlayer.getCredit()));
            gameLog.setThisPlayersTurn(currentPlayer.isThisPlayersTurn());
            gameLog.setNextPlayersTurn(nextPlayer.isThisPlayersTurn());

        }

        if(action == Action.CHECK){
            //option to "check" if no betting action has occurred beforehand.
            // A check simply means to pass the action to the next player in the hand.
            currentPlayer.check();
            game.setTransactionNr(game.getTransactionNr() + 1);
            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);
            gameLog.setTransactionNr(game.getTransactionNr());


            gameLog.setTransactionNr(game.getTransactionNr());
            gameLog.setGameRound(game.getGameRound());
            gameLog.setAction(Action.CHECK);
            gameLog.setPlayers(game.getPlayers());
            gameLog.setActivePlayers(game.getActivePlayers());
            gameLog.setRevealedCards(game.getTableCards());
            gameLog.setGameName(game.getGameName());
            gameLog.setRaiseAmount(0);
            gameLog.setPlayerName(currentPlayer.getPlayerName());
            gameLog.setPlayerId(currentPlayer.getId());
            gameLog.setNextPlayerName(nextPlayer.getPlayerName());
            gameLog.setNextPlayerId(nextPlayer.getId());
            gameLog.setPlayerPot(currentPlayer.getAmountInPot());
            gameLog.setPotAmount(pot.getAmount());
            gameLog.setRoundOver(game.isRoundOver());
            gameLog.setGameOver(game.isGameOver());
            gameLog.setAmountToCall(0);
            gameLog.setThisPlayersTurn(currentPlayer.isThisPlayersTurn());
            gameLog.setNextPlayersTurn(nextPlayer.isThisPlayersTurn());



        }
        if(action == Action.MAKEPLAYER){

        }
        if(action == Action.MAKESPECTATOR){

        }


//enter the possible amount to bet for the next player
        int amountToRaiseSoCreditOfNextPlayerIsZero = nextPlayer.getCredit()-(getPlayerWithMostAmountInPot(game).getAmountInPot()-nextPlayer.getAmountInPot());
        int lowerLimit = bigBlind;
        int higherLimit = 2 * bigBlind;
        if (game.getPotType().equals("no limit")) {
            gameLog.setPossibleRaiseAndBetAmount(amountToRaiseSoCreditOfNextPlayerIsZero);
        }
        if (game.getPotType().equals("pot limit")){
            gameLog.setPossibleRaiseAndBetAmount(Math.min(amountToRaiseSoCreditOfNextPlayerIsZero, game.getPot().getAmount()));
        }
        if (game.getPotType().equals("fixed limit")){
            if (game.getGameRound()==GameRound.Preflop || game.getGameRound() == GameRound.Flop){
                gameLog.setPossibleRaiseAndBetAmount(Math.min(lowerLimit, amountToRaiseSoCreditOfNextPlayerIsZero));
            }else{
                gameLog.setPossibleRaiseAndBetAmount(Math.min(higherLimit, amountToRaiseSoCreditOfNextPlayerIsZero));
            }
        }
        if (game.getPotType().equals("split limit")){
            if (game.getGameRound()==GameRound.Preflop || game.getGameRound() == GameRound.Flop){
                gameLog.setPossibleRaiseAndBetAmount(Math.min(amountToRaiseSoCreditOfNextPlayerIsZero, lowerLimit));
            }else{
                gameLog.setPossibleRaiseAndBetAmount(Math.min(amountToRaiseSoCreditOfNextPlayerIsZero, higherLimit));
            }
        }


// enters the posssible actions for the next player into the gameLog
        List <Action> possibleActions = game.getPossibleActions();

        //second player of first round has to raise
        if (action == Action.BET && pot.getAmount() == smallBlind && game.getGameRound() == GameRound.Preflop && currentPlayer == activePlayers.get(0)) {
            possibleActions.clear();
            if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                possibleActions.add(Action.RAISE);
            }
            possibleActions.add(Action.FOLD);
            //third player of first round can raise call or fold
        }else if (action == Action.BET && pot.getAmount() == bigBlind && game.getGameRound() == GameRound.Preflop && currentPlayer == activePlayers.get(1)) {
            possibleActions.clear();
            if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                possibleActions.add(Action.RAISE);
            }
            possibleActions.add(Action.CALL);
            possibleActions.add(Action.FOLD);
        }


        //game no limit and pot limit
        else if(game.getPotType().equals("no limit") || game.getPotType().equals("pot limit")) {
            if (action == Action.BET) {
                possibleActions.clear();
                possibleActions.clear();
                if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                    possibleActions.add(Action.RAISE);
                }
                    possibleActions.add(Action.CALL);
                possibleActions.add(Action.FOLD);
            }
            else if (action == Action.RAISE) {
                possibleActions.clear();
                if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                    possibleActions.add(Action.RAISE);
                }
                    possibleActions.add(Action.CALL);
                possibleActions.add(Action.FOLD);
                possibleActions.add(Action.FOLD);
            }
            else if (action == Action.CALL) {
                possibleActions.clear();
                if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                    possibleActions.add(Action.RAISE);
                }
                possibleActions.add(Action.FOLD);
                if (nextPlayer.getAmountInPot() < currentPlayer.getAmountInPot()) {
                    possibleActions.add(Action.CALL);
                }
                //checking is only possible if all players have same amount in pot
                if (nextPlayer.getAmountInPot() == currentPlayer.getAmountInPot() + amount) {
                    possibleActions.add(Action.CHECK);
                }
            }
            else if (action == Action.CHECK) {
                possibleActions.clear();
                if (game.getActionsAfterRaise() == 0) {
                    possibleActions.add(Action.BET);
                }
                else {
                    if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                        possibleActions.add(Action.RAISE);
                    }
                }
                possibleActions.add(Action.CHECK);
                possibleActions.add(Action.FOLD);
            }
        }

            //game fixed limit and split limit
        else if(game.getPotType().equals("fixed limit") || game.getPotType().equals("split limit")) {
            if (action == Action.BET) {
                possibleActions.clear();
                if    ((game.getGameRound()==GameRound.Preflop && game.getTimesRaisedPerPreflop() <= 2)||
                        (game.getGameRound()==GameRound.Flop && game.getTimesRaisedPerFlop() <= 2)||
                        (game.getGameRound()==GameRound.RiverCard && game.getTimesRaisedRiverCard() <= 2)||
                        (game.getGameRound()==GameRound.TurnCard && game.getTimesRaisedTurnCard() <= 2)) {
                    if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                        possibleActions.add(Action.RAISE);
                    }
                }
                    possibleActions.add(Action.CALL);
                possibleActions.add(Action.FOLD);
            }
            else if (action == Action.RAISE) {
                if (game.getGameRound()==GameRound.Preflop){game.setTimesRaisedPerPreflop(game.getTimesRaisedPerPreflop()+1);}
                if (game.getGameRound()==GameRound.Flop){game.setTimesRaisedPerFlop(game.getTimesRaisedPerFlop()+1);}
                if (game.getGameRound()==GameRound.RiverCard){game.setTimesRaisedRiverCard(game.getTimesRaisedRiverCard()+1);}
                if (game.getGameRound()==GameRound.TurnCard){game.setTimesRaisedTurnCard(game.getTimesRaisedTurnCard()+1);}
                possibleActions.clear();
                if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                    possibleActions.add(Action.RAISE);
                }
                    possibleActions.add(Action.CALL);
                possibleActions.add(Action.FOLD);

                //per round is it not possible to raise more than three times
                if     ((game.getGameRound()==GameRound.Preflop && game.getTimesRaisedPerPreflop() > 2)||
                        (game.getGameRound()==GameRound.Flop && game.getTimesRaisedPerFlop() > 2)||
                        (game.getGameRound()==GameRound.RiverCard && game.getTimesRaisedRiverCard() > 2)||
                        (game.getGameRound()==GameRound.TurnCard && game.getTimesRaisedTurnCard() > 2)) {
                    possibleActions.clear();
                        possibleActions.add(Action.CALL);
                    possibleActions.add(Action.FOLD);
                }
            }
            else if (action == Action.CALL) {
                possibleActions.clear();
                if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                    possibleActions.add(Action.RAISE);
                }
                possibleActions.add(Action.FOLD);
                if (nextPlayer.getAmountInPot() < currentPlayer.getAmountInPot()) {
                    possibleActions.add(Action.CALL);
                }
                //checking is only possible if all players have same amount in pot
                if (nextPlayer.getAmountInPot() == currentPlayer.getAmountInPot() + amount) {
                    possibleActions.add(Action.CHECK);
                }
            }
            else if (action == Action.CHECK) {
                possibleActions.clear();
                if (game.getActionsAfterRaise() == 0) {
                    possibleActions.add(Action.BET);
                }
                else if    ((game.getGameRound()==GameRound.Preflop && game.getTimesRaisedPerPreflop() <= 2)||
                            (game.getGameRound()==GameRound.Flop && game.getTimesRaisedPerFlop() <= 2)||
                            (game.getGameRound()==GameRound.RiverCard && game.getTimesRaisedRiverCard() <= 2)||
                            (game.getGameRound()==GameRound.TurnCard && game.getTimesRaisedTurnCard() <= 2)) {
                    if (!(amountToRaiseSoCreditOfNextPlayerIsZero == 0)) {
                        possibleActions.add(Action.RAISE);
                    }
                }

                possibleActions.add(Action.CHECK);
                possibleActions.add(Action.FOLD);
            }
        }


        //if all player folded, there is no winner
       if (activePlayers.size() == 1){
           List<Player> winners = gameLog.getWinners();
           winners.clear();
           winners.add(activePlayers.get(0));
           gameLog.setWinners(winners);
           gameLog.setWinnerComboValue("Everyone else Folded or left");
           activePlayers.get(0).addCredit(game.getPot().getAmount());
           game.getPot().removeAmount(game.getPot().getAmount());
           game.startNewRound();

       }



        //special case:
        //if one player bets bigger amount than other players have as credit:
        //it is played only one more round
        //the players who have less credit than the hasRaisedBig player has in the pot must call the whole amount they have as credit or fold
        //the other players can make a normal call
        //if one of the hasRaisedBig players wins, he gets the whole amount in the pot
        // if one of the other players win they get from each of the players, the amount he himself has in the pot or if one player has betted less his whole credit
        //the rest amount in the pot is given back to the not winner players, each gets the difference = max (notWinnerPlayer.amountInPot-WinnerPlayer.amount in pot or, 0)
        //at end start new round or set gameOver
        //return so rest of this method is not visited













//if one player goes all in, all other players can only call or fold until the players turn again
//when players turn again, this gameround is over
//all remining card are shown and the winner is calculated, money is given to the winner
//start the new round

       for (int i = 0; i<activePlayers.size();i++){
           if (activePlayers.get(i).getCredit() <= 0){
               if (action == Action.CALL && game.isPlayerWentAllIN() == false){
                   game.setaPlayerHasNullCredit(true);
               }
               if (action == Action.RAISE || action == Action.BET) {
                   game.setPlayerWentAllIN(true);
                   possibleActions.clear();
                   possibleActions.add(Action.CALL);
                   possibleActions.add(Action.FOLD);
                   gameLog.setPossibleActions(possibleActions);
               }
           }
       }

        if (game.isaPlayerHasNullCredit() && (action == Action.CALL || action == Action.RAISE)){
            game.setActionsAfterNullCredit(game.getActionsAfterNullCredit()+1);
        }

       if (game.isPlayerWentAllIN() && (action == Action.CALL)){
           game.setActionsAfterAllIN(game.getActionsAfterAllIN()+1);
       }
       if (game.getActionsAfterAllIN() == activePlayers.size()-1 || game.getActionsAfterNullCredit() == activePlayers.size()-1){
           game.setRoundOver(true);


           if (game.getGameRound() == GameRound.Preflop){
               game.addTableCard();
               game.addTableCard();
               game.addTableCard();
               game.addTableCard();
               game.addTableCard();
           }else if (game.getGameRound() == GameRound.Flop){
               game.addTableCard();
               game.addTableCard();
           }else if (game.getGameRound() == GameRound.TurnCard) {
               game.addTableCard();
           }
           gameLog.setRevealedCards(game.getTableCards());


           //calculate the winners
           List<Player> winners;
           WinnerCalculator winnerCalculator = new WinnerCalculator();
           winners = winnerCalculator.isWinner(activePlayers, game.getTableCards());
           gameLog.setWinners(winners);
           //calculate the amount won by every winner if every player has same amount in pot
           int wonAmount = pot.getAmount()/winners.size();
           gameLog.setWonAmount(wonAmount);
           //add won amount to the credit of the winnerPlayers
           for (int i =0; i< winners.size(); i++){
               winners.get(i).addCredit(wonAmount);
           }
           pot.removeAmount(pot.getAmount());
           gameLog.setPotAmount(0);
           gameLog.setPlayers(players);
           //calculate the amount won by every winner if not every player has the same amount in pot


/*
            Player playerWithMostAmountInPot = players.get(0);
            for (int i = 1; i < players.size(); i++) {
                if (players.get(i).getAmountInPot() > playerWithMostAmountInPot.getAmountInPot()) {
                    playerWithMostAmountInPot = players.get(i);
                }
            }

            boolean allWinnersHaveMostAmountInPot = true;
            for (int a = 0; a < winners.size(); a++) {
                //if a winner has not most amount in pot
                if (winners.get(a).getAmountInPot() != playerWithMostAmountInPot.getAmountInPot()) {
                    allWinnersHaveMostAmountInPot = false;
                    break;
                }
            }

            boolean winnersHaveSameAmountInPot = true;
            for (int i = 0; i < winners.size(); i++) {
                if (winners.get(i).getAmountInPot() != winners.get(0).getAmountInPot()) {
                    winnersHaveSameAmountInPot = false;
                    break;
                }
            }

            //if all winners have most amount in pot
            if (allWinnersHaveMostAmountInPot && winnersHaveSameAmountInPot) {
                int wonAmount = pot.getAmount() / winners.size();
                gameLog.setWonAmount(wonAmount);
                //add won amount to the credit of the winnerPlayers
                for (int i = 0; i < winners.size(); i++) {
                    winners.get(i).addCredit(wonAmount);
                }
                for (int a = 0; a < players.size(); a++) {
                    players.get(a).setAmountInPot(0);
                }

                pot.removeAmount(pot.getAmount());
                gameLog.setPotAmount(0);
                gameLog.setPlayers(players);
            }
            else if (winnersHaveSameAmountInPot) {
                //if not all winners have not most amount in pot
                //if  winners have same amount in pot but less than other players, they get what they have betted in relation and all other players get amount back
                int winnerAmount = 0;
                for (Player player : players) {
                    if (player.getAmountInPot() > winners.get(0).getAmountInPot()) {
                        winnerAmount = winnerAmount + winners.get(0).getAmountInPot();
                        player.setAmountInPot(player.getAmountInPot() - winners.get(0).getAmountInPot());
                        game.getPot().removeAmount(winners.get(0).getAmountInPot());


                    }
                    else {
                        winnerAmount = winnerAmount + player.getAmountInPot();
                        game.getPot().removeAmount(player.getAmountInPot());

                    }
                    player.addCredit(player.getAmountInPot());
                    game.getPot().removeAmount(player.getAmountInPot());
                    player.setAmountInPot(0);
                }
                winnerAmount = winnerAmount / winners.size();
                winnerAmount = winnerAmount + winners.get(0).getAmountInPot();
                for (int i = 0; i < winners.size(); i++) {
                    winners.get(i).addCredit(winnerAmount);
                    game.getPot().removeAmount(winners.get(i).getAmountInPot());
                }
            }
            else {
                //winners have not same Amount in pot and
                //if there are winners with most amount in pot and winners with not most amount in pot, they split in ralation amountin pot


            }

*/
           int playersWithCredit = 0;
           for (int i = 0;i < players.size();i++){
               if (players.get(i).getCredit()>0){
                   playersWithCredit++;
               }
           }
           if (playersWithCredit <2) {
               game.setGameOver(true);
               gameLog.setGameOver(true);
               possibleActions.clear();
               gameLog.setPossibleActions(possibleActions);
           }else{
               game.startNewRound();
           }
       }

/*


       //check if game is over

       if (game.playOneMoreRoundToGameOver(currentPlayer)) {
                if (game.getGameRound() == GameRound.Preflop){
                    game.addTableCard();
                    game.addTableCard();
                    game.addTableCard();
                    game.addTableCard();
                    game.addTableCard();
                }else if (game.getGameRound() == GameRound.Flop){
                    game.addTableCard();
                    game.addTableCard();
                }else if (game.getGameRound() == GameRound.RiverCard) {
                    game.addTableCard();
                }
                gameLog.setGameOver(true);
                gameLog.setRevealedCards(game.getTableCards());
                game.setGameOver(true);
       }

*/

        /*
        int playersWithCredit = 0;
        for (int i = 0;i < players.size();i++){
            if (players.get(i).getCredit()>0){
                playersWithCredit++;
            }
        }
        if (playersWithCredit <2) {
            gameLog.setPlayOneMoreRound(true);
            game.setPlayerWithZeroCredit(currentPlayer);
        }
        */



/*
        if (game.isGameOver() == true){
            game.setRoundOver(true);
            gameLog.setRoundOver(true);
            //calculate the winners
            List<Player> winners;
            WinnerCalculator winnerCalculator = new WinnerCalculator();
            winners = winnerCalculator.isWinner(activePlayers, game.getTableCards());
            gameLog.setWinners(winners);
            //calculate the amount won by every winner
            int wonAmount = pot.getAmount()/winners.size();
            gameLog.setWonAmount(wonAmount);
            //add won amount to the credit of the winnerPlayers
            for (int i =0; i< winners.size(); i++){
                winners.get(i).addCredit(wonAmount);
            }
            pot.removeAmount(pot.getAmount());
            gameLog.setPotAmount(0);
            gameLog.setPlayers(players);

            int playersWithCredit = 0;
            for (int i = 0;i < players.size();i++){
                if (players.get(i).getCredit()>0){
                    playersWithCredit++;
                }
            }

            //if now both reminding players have credit again, game goes on

            if (playersWithCredit >=2){
                game.setGameOver(false);
                game.setRoundOver(true);
                gameLog.setGameOver(false);
                gameLog.setRoundOver(true);
                //open new round again
                game.startNewRound();
            }


        }
*/
//if next Player went all in he is not allowed to play

        if (game.getNextPlayer(currentPlayer).getCredit() <= 0) {
            Player player = currentPlayer;
            while (game.getNextPlayer(player).getCredit()<= 0) {
                player = game.getNextPlayer(player);
            }
            gameLog.setNextPlayerName(game.getNextPlayer(player).getPlayerName());
            gameLog.setNextPlayerId(game.getNextPlayer(player).getId());
        }



        //if the game is over
        if (game.isGameOver()==true){
            possibleActions.clear();
        }





        gameLog.setPossibleActions(possibleActions);
        return gameLog;
    }



    public List<Card> getTableCards(String token){
        Game game = this.gameSelect.getGameByToken(token);
        return game.getTableCards();
    }

    public long getNextId(){
        currentId += 1;
        return currentId;
    }

    public void updateBlinds(Game game){

        /*
        input is the game that needs to update blinds
        -the small blind of the previous round becomes a "NO Blind"
        -the big blind of the previous round becomes the small blind
         -the player after the previous big blind becomes the new big blind
         */

        /*List<Player> players = game.getPlayers();


        Player player = players.get(0);
        players.remove(0);
        players.add(players.size()+1, player);

        game.setSmallBlind(players.get(0));
        game.setBigBlind(players.get(1));*/

    }

    public void toggleReadyStatus(long gameId, long playerId){}

    public List<Player> getPlayers(long gameId){
        return gameSelect.getGameById(gameId).getPlayers();
    }

    public void removePlayer(long gameId, long userId){
        Game game = getGame(gameId);
        Player player = game.getPlayerById(userId);
        List<Player> players= game.getPlayers();
        List<Player> activePlayers = game.getActivePlayers();
        GameLog gameLog = game.getGameLog();

        User user = userService.getUserById(userId);
        user.setBalance2(player.getCredit());

        player.setAmountInPot(0);

        getGame(gameId).getPlayers().remove(getGame(gameId).getPlayerById(userId));
        getGame(gameId).getGameLog().setPlayers(getGame(gameId).getPlayers());

        if (userId == game.getGameLog().getNextPlayerId()) {
            game.getGameLog().setNextPlayerId(game.getNextPlayer(player).getId());
            game.getGameLog().setNextPlayerName(game.getNextPlayer(player).getPlayerName());

            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            game.getNextPlayer(player).setThisPlayersTurn(true);
        }

        activePlayers.remove(player);

        if (players.size()<2){
            game.setGameOver();
            gameLog.setGameOver(true);
            game.setRoundOver();
            gameLog.setRoundOver(true);
            activePlayers.get(0).addCredit(game.getPot().getAmount());
            User winner = userService.getUserById(activePlayers.get(0).getId());
            winner.setBalance2(activePlayers.get(0).getCredit());
            gameLog.setWinners(activePlayers);
            gameLog.setActivePlayers(activePlayers);
            List<Action> possibleActions = game.getPossibleActions();
            possibleActions.clear();
            gameLog.setPossibleActions(possibleActions);
            game.getPot().removeAmount(game.getPot().getAmount());
            gameLog.setPotAmount(game.getPot().getAmount());

        }else if (activePlayers.size()<2){
            game.setRoundOver();
            gameLog.setRoundOver(true);
            activePlayers.get(0).addCredit(game.getPot().getAmount());
            gameLog.setActivePlayers(activePlayers);
            game.getPot().removeAmount(game.getPot().getAmount());
            gameLog.setPotAmount(game.getPot().getAmount());
            game.startNewRound();
        }

        /*
        Game game = gameSelect.getGameById(gameId);
        Player player = game.getPlayerById(userId);
        userService.addBalanceOfPlayer(userId, player.getCredit());
        game.getActivePlayers().remove(player);
        */
    }

    public List<Game> getAllGames(){return gameSelect.getAllGames();}

    public boolean checkAuthorizationPut(long gameId, long playerId, String token){
        User user = userService.getUserById(playerId);
        Game game = gameSelect.getGameById(gameId);
        List<Player> players = game.getPlayers();
        boolean playerIsInGame = false;
        for (int i = 0; i<players.size();i++){
            if (players.get(i).getId() == playerId){
                playerIsInGame = true;
            }
        }
        if (user.getToken().equals(token)&& playerIsInGame == true){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkAuthorizationGet(String token, long gameId) {
        User user = userService.getUserByToken(token);
        Game game = gameSelect.getGameById(gameId);
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if ((game.getPlayers().get(i).getId() == user.getId()) && (user != null)) {
                return true;
            }else{
                continue;
            }
        }
        return false;
    }



    private void updateActiveUsers(Game game){
        final int SMALL_BLIND = 5;
        for(Player player : game.getPlayers()){
            if(player.getCredit() < SMALL_BLIND){
                game.removePlayer(player);
            }
        }
    }

    public Game findGameById(long gameId){
        return gameSelect.getGameById(gameId);
    }

    public GameLog roundStart(Game game){

        /*FOR TESTING PURPOSES*/
        GameLog gameLog = game.getGameLog();

        User user1 = new User();
        user1.setUsername("MOCKUSER1");
        user1.setPassword("password");
        user1.setToken("token");
        user1.setId((long) 2);

        User user2 = new User();
        user2.setUsername("MOCKUSER2");
        user2.setPassword("password2");
        user2.setToken("token2");
        user2.setId((long) 3);

        Player player1 = new Player(user1);
        Player player2 = new Player(user2);

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addActivePlayer(player1);
        game.addActivePlayer(player2);

        /*END TESTING PURPOSES*/

        //all players with a credit > Small_blind remain in the game
        updateActiveUsers(game);
        updateBlinds(game);

        List<Action> possibleActions = new ArrayList<>();
        possibleActions.add(Action.BET);
        possibleActions.add(Action.FOLD);
        possibleActions.add(Action.MAKESPECTATOR);
        game.setPossibleActions(possibleActions);

        gameLog.setTransactionNr(game.getTransactionNr());
        gameLog.setGameRound(GameRound.Preflop);
        gameLog.setAction(Action.BET);
        gameLog.setPlayers(game.getPlayers());
        gameLog.setActivePlayers(game.getActivePlayers());
        gameLog.setRevealedCards(game.getTableCards());
        gameLog.setGameName(game.getGameName());
        gameLog.setRaiseAmount(0);
        gameLog.setPlayerName(game.getActivePlayers().get(0).getPlayerName());
        gameLog.setPlayerId(game.getActivePlayers().get(0).getId());
        gameLog.setNextPlayerName(game.getActivePlayers().get(1).getPlayerName());
        gameLog.setNextPlayerId(game.getActivePlayers().get(1).getId());
        gameLog.setPlayerPot(0);
        gameLog.setPotAmount(0);
        gameLog.setRoundOver(false);
        gameLog.setGameOver(false);
        gameLog.setAmountToCall(0);
        gameLog.setThisPlayersTurn(false);
        gameLog.setNextPlayersTurn(true);
        gameLog.setPossibleActions(game.getPossibleActions());


        return gameLog;
    }

}
