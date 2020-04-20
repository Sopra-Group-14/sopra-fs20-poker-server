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
    //private final Logger gameLog = LoggerFactory.getLogger(GameService.class);

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
        newGame.setGameHostID(hostID);
        newGame.setGameHostName(hostName);
        newGame.setHostToken(hostToken);

        this.gameSelect.addGame(newGame);

        return newGame;

    }

    public Game getGame(){

        return null;
    }

    public void addHost(long hostID, Game game){

        User playerHost = userService.getUserById(hostID);
        Player player = userService.setToPlayer(playerHost);

        game.addPlayer(player);
        game.addActivePlayer(player);


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

        if (action == Action.BET){
            //the bet amount mustn't be bigger than the actual credit of the player
            if (amount > currentPlayer.getCredit()) {
                String baseErrorMessage = "A call involves matching the amount already bet. The credit of the Player %s is too low!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }
            //In the PreFlop the first player must bet, he can decide if he goes with the bigblind or raises

            //In the rounds Flop, Turn, river a player can bet if no player has bet before him (all players before have checked or folded)


        }

        if(action == Action.FOLD){

            game.playerFolds(currentPlayer);

        }
        if(action == Action.RAISE){

            //the raised amount + the called amount mustn't be bigger than the actual credit of the player
            int addedAmount = (previousPlayer.getAmountInPot()-currentPlayer.getAmountInPot()) + amount;
            int amountToCall = (previousPlayer.getAmountInPot() - currentPlayer.getAmountInPot());
            if (addedAmount > currentPlayer.getCredit()) {
                String baseErrorMessage = "A call involves matching the amount already bet. The credit of the Player %s is too low!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }

           /* //the raised amount must be bigger than the difference of the amount in the pot of previous player and next player
            if (amount+ currentPlayer.getAmountInPot()< <= previousPlayer.getAmountInPot()-currentPlayer.getAmountInPot()){
                String baseErrorMessage = "The specified amount is below the min.raise, raising below call amount (%d) is not allowed!";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage, previousPlayer.getAmountInPot()-currentPlayer.getAmountInPot()));
            }*/

            //the raised amount must be bigger or equal than 1
            if (amount<=1){
                String baseErrorMessage = "The Player %s tries to raise by an amount lower than 1!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }


                currentPlayer.removeCredit(addedAmount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + addedAmount);
                pot.addAmount(addedAmount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        game.getGameRound(),
                        Action.RAISE,
                        game.getPlayers(),
                        game.getActivePlayers(),
                        game.getTableCards(),
                        game.getGameName(),
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
            //if current player is the first player of the round preflop then he is not allowed to call, but has to bet or raise
            if (game.getGameRound() == GameRound.Preflop && activePlayers.get(0) == currentPlayer && currentPlayer.getAmountInPot() == 0){
                String baseErrorMessage = "The player &s is not allowed to call, he has to bet or raise!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }
            //currentPlayer is only allowed to call if the player before has raised or betted
            if (!(previousPlayer.getAmountInPot() > currentPlayer.getAmountInPot())){
                String baseErrorMessage = "The player &s is not allowed to call, he has to bet or raise!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }

            //check if called amount is equal to the difference in the pot between the amounts of the previous and current player
            if (previousPlayer.getAmountInPot() - currentPlayer.getAmountInPot() != amount) {
                String baseErrorMessage = "the amount to call does not match the difference between the amount of Player %s and Player %s in the pot";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName(), previousPlayer.getPlayerName()));
            }

                currentPlayer.removeCredit(amount);
                currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
                pot.addAmount(amount);
                game.setTransactionNr(game.getTransactionNr() + 1);
                gameLog = new GameLog(game.getTransactionNr(),
                        game.getGameRound(),
                        Action.CALL,
                        game.getPlayers(),
                        game.getActivePlayers(),
                        game.getTableCards(),
                        game.getGameName(),
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

            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);
            return gameLog;
        }

        if(action == Action.CHECK){

            currentPlayer.check();

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

        GameLog gameLog = game.roundStartGameLog(game);

        /*FOR TESTING PURPOSES*/

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


        System.out.print(gameLog);
        updateActiveUsers(game);
        updateBlinds(game);

        return gameLog;
    }

}
