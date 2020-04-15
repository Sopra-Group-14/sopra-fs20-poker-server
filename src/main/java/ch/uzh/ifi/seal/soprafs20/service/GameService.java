package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.BigBlind;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Pot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.NumberUp;
import java.util.Iterator;
import java.util.List;

import static ch.uzh.ifi.seal.soprafs20.entity.GameSelect.NULL_GAME;
import static java.lang.Math.random;

/**
 * The GameService takes care of what happens with Games.
 */
@Service
@Transactional
public class GameService {

    private final UserService userService;
    private final Logger gameLog = LoggerFactory.getLogger(GameService.class);

    //The GameSelect is essentially just a list that holds all games. The games can then be accessed through it.
    private final GameSelect gameSelect = new GameSelect();

    private int currentId = 0;
    private String gameName;
    private long hostID;
    private String potType;

    public GameService(UserService userService){
        this.userService = userService;
    }

    public Game createGame(String gameName, long hostID, String potType){

        /*createGame receives gameName, hostId and potType from client. The game constructor expects the hostName;

         */

        //TODO resolve null pointer exception if user that does not exist creates a game
        User host = userService.getUserById(hostID);

        String hostName = host.getUsername();
        String hostToken = host.getToken();
        Game newGame = new Game();

        long currentId;
        if(this.gameSelect.getAllGames() != null){
            currentId = this.gameSelect.getAllGames().size() + 1;
        }else{
            currentId = 1;
        }

        newGame.setGameId(currentId);
        newGame.setGameName(gameName);
        newGame.setPotType(potType);
        newGame.setGameHostName(hostName);
        newGame.setHostToken(hostToken);

        this.gameSelect.addGame(newGame);

        return newGame;

    }

    public void addHost(long hostID, Game game){

        User playerHost = userService.getUserById(hostID);
        Player player = userService.setToPlayer(playerHost);

        game.addPlayer(player);

    }

    public GameLog executeAction(Action action, int amount, long gameId, long playerId, String token){

        GameLog gameLog;
        Game game = gameSelect.getGameById(gameId);
        List<Player> players = game.getPlayers();
        List<Player> activePlayers = game.getActivePlayers();
        Player currentPlayer = game.getCurrentPlayer(playerId);
        Player previousPlayer = game.getPreviousPlayer(currentPlayer);
        Player nextPlayer = game.getNextPlayer(currentPlayer);
        Pot pot = new Pot();


        //The player tries to take an action when it is not their turn
        if (!activePlayers.contains(currentPlayer) |  !currentPlayer.isThisPlayersTurn()){
            String baseErrorMessage = "Not Player %s turn!";
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
        }

        //The player ID does not exist
        if (!players.contains(currentPlayer)){
            String baseErrorMessage = "Player %s with Id %d does not exist in the game with id %d !";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId(), game.getGameId()));
        }

        //The user has already folded
        if (!activePlayers.contains(currentPlayer) | currentPlayer.hasFolded()==true){
            String baseErrorMessage = "The Player %s with Id %d has already folded and does not participate in the current round anymore!";
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
        }



        if(action == Action.FOLD){

        }
        if(action == Action.RAISE){
            //the raised amount mustn't be bigger than the actual credit of the player
            if (amount > currentPlayer.getCredit()) {
                String baseErrorMessage = "A call involves matching the amount already bet. The credit of the Player %s is too low!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }

            if (amount < previousPlayer.getAmountInPot()-currentPlayer.getAmountInPot()){
                String baseErrorMessage = "The specified amount is below the min.raise, raising below call amount (%d) is not allowed!";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage, previousPlayer.getAmountInPot()-currentPlayer.getAmountInPot()));
            }
            if (amount<1){
                String baseErrorMessage = "The Player %s tries to raise by an amount lower than 1!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }
            if (game.getGameRound() == GameRound.Preflop && activePlayers.get(0) == currentPlayer && currentPlayer.getAmountInPot() == 0) {
                //TODO check if raised amount is same as small blind or higher
                //if (amount >= bigBlind.getCredit()) {
                currentPlayer.removeCredit(amount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        GameRound.Preflop,
                        Action.RAISE,
                        amount,
                        currentPlayer.getPlayerName(),
                        currentPlayer.getId(),
                        nextPlayer.getPlayerName(),
                        nextPlayer.getId(),
                        currentPlayer.getAmountInPot(),
                        pot.getAmount(),
                        game.isRoundOver(),
                        game.isGameOver(),
                        amount);
                /*}
                   else{
                       String baseErrorMessage = "The amount, which want to be called must be equal or bigger than the BigBlind in the first round";
                       throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                    }*/
            }
            else if (game.getGameRound() == GameRound.Preflop && activePlayers.get(1) == currentPlayer && currentPlayer.getAmountInPot() == 0) {
                //TODO check if called amount is same as big blind or higher
                //if (amount >= bigBlind.getCredit()) {
                currentPlayer.removeCredit(amount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        GameRound.Preflop,
                        Action.RAISE,
                        amount,
                        currentPlayer.getPlayerName(),
                        currentPlayer.getId(),
                        nextPlayer.getPlayerName(),
                        nextPlayer.getId(),
                        currentPlayer.getAmountInPot(),
                        pot.getAmount(),
                        game.isRoundOver(),
                        game.isGameOver(),
                        amount);
                /*}
                   else{
                       String baseErrorMessage = "The amount, which want to be called must be equal or bigger than the BigBlind in the first round";
                       throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                    }*/
            }
            else {currentPlayer.removeCredit(amount);
                int amountToCall = (previousPlayer.getAmountInPot() - currentPlayer.getAmountInPot());
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        game.getGameRound(),
                        Action.RAISE,
                        amount,
                        currentPlayer.getPlayerName(),
                        currentPlayer.getId(),
                        nextPlayer.getPlayerName(),
                        nextPlayer.getId(),
                        currentPlayer.getAmountInPot(),
                        pot.getAmount(),
                        game.isRoundOver(),
                        game.isGameOver(),
                        amountToCall);

            }

            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);
            //return gameLog to GameController
            return gameLog;
        }

        if(action == Action.CALL) {
            //the called amount mustn't be bigger than the actual credit of the player.
            if (amount > currentPlayer.getCredit()) {
                String baseErrorMessage = "A call involves matching the amount already bet. The credit of the Player %s is too low!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }

            //check if the player playing is the first player of first round.
            //In the first betting round, the amount bet must be higher or equal than the big blind
            if (game.getGameRound() == GameRound.Preflop && activePlayers.get(0) == currentPlayer && currentPlayer.getAmountInPot() == 0) {
                //TODO check if called amount is same as small blind or higher
                //if (amount >= bigBlind.getCredit()) {
                currentPlayer.removeCredit(amount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        GameRound.Preflop,
                        Action.CALL,
                        amount,
                        currentPlayer.getPlayerName(),
                        currentPlayer.getId(),
                        nextPlayer.getPlayerName(),
                        nextPlayer.getId(),
                        currentPlayer.getAmountInPot(),
                        pot.getAmount(),
                        game.isRoundOver(),
                        game.isGameOver(),
                        amount);
                /*}
                   else{
                       String baseErrorMessage = "The amount, which want to be called must be equal or bigger than the BigBlind in the first round";
                       throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                    }*/

            }else if (game.getGameRound() == GameRound.Preflop && activePlayers.get(1) == currentPlayer && currentPlayer.getAmountInPot() == 0){
                //TODO check if called amount is same as big blind or higher
                //if (amount >= bigBlind.getCredit()) {
                currentPlayer.removeCredit(amount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        GameRound.Preflop,
                        Action.CALL,
                        amount,
                        currentPlayer.getPlayerName(),
                        currentPlayer.getId(),
                        nextPlayer.getPlayerName(),
                        nextPlayer.getId(),
                        currentPlayer.getAmountInPot(),
                        pot.getAmount(),
                        game.isRoundOver(),
                        game.isGameOver(),
                        amount);
                /*}
                   else{
                       String baseErrorMessage = "The amount, which want to be called must be equal or bigger than the BigBlind in the first round";
                       throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                    }*/



            }
            else {   //check if called amount is equal to the difference in the pot between the amounts of the previous and current player
                    if (previousPlayer.getAmountInPot() - currentPlayer.getAmountInPot() != amount) {
                        String baseErrorMessage = "the amount to call does not match the difference between the amount of Player %s and Player %s in the pot";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName(), previousPlayer.getPlayerName()));
                    }
                    else {
                        currentPlayer.removeCredit(amount);
                        currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                        pot.addAmount(amount);
                        game.setTransactionNr(game.getTransactionNr() + 1);
                        gameLog = new GameLog(game.getTransactionNr(),
                                game.getGameRound(),
                                Action.CALL,
                                amount,
                                currentPlayer.getPlayerName(),
                                currentPlayer.getId(),
                                nextPlayer.getPlayerName(),
                                nextPlayer.getId(),
                                currentPlayer.getAmountInPot(),
                                pot.getAmount(),
                                game.isRoundOver(),
                                game.isGameOver(),
                                amount);
                        }

                }


            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (int i = 0; i<activePlayers.size(); i++){
                activePlayers.get(i).setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);


            return gameLog;
        }

        if(action == Action.CHECK){


        }
        if(action == Action.MAKEPLAYER){

        }
        if(action == Action.MAKESPECTATOR){

        }
        return null;
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

        List<Player> players = game.getPlayers();
        game.setSmallBlind(players.get(0));
        game.setBigBlind(players.get(1));

    }

    public void toggleReadyStatus(long gameId, long playerId){}

    public List<Player> getPlayers(long gameId){
        return gameSelect.getGameById(gameId).getPlayers();
    }

    public void removePlayer(long gameId, long userId){}

    public List<Game> getAllGames(){return gameSelect.getAllGames();}

    public boolean checkAuthorizationPut(long gameId, long playerId, String token){
        User user = userService.getUserById(playerId);
        if (user.getToken().equals(token)){
            return true;
        }
        else{
            return false;
        }
    }

}
