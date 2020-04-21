package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.GameRound;
import ch.uzh.ifi.seal.soprafs20.controller.GameController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        int smallBlind = 5;
        int bigBlind = 10;
        int timesRaisedPerRound = 0;
        Player playerWithMostAmountInPot;
        playerWithMostAmountInPot = game.getActivePlayers().get(0);
        for (int i = 0; i<game.getActivePlayers().size();i++){
            if (playerWithMostAmountInPot.getAmountInPot() <= game.getActivePlayers().get(i).getAmountInPot()){
                playerWithMostAmountInPot = game.getActivePlayers().get(i);
            }
        }


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
        if (!activePlayers.contains(currentPlayer) | currentPlayer.hasFolded()){
            String baseErrorMessage = "The Player %s with Id %d has already folded and does not participate in the current round anymore!";
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
        }


        //the first player of first round (preflop) has to bet the small blind
        if (game.getGameRound() == GameRound.Preflop && currentPlayer == activePlayers.get(0)){
            if (action != Action.BET || amount != smallBlind){
                String baseErrorMessage = "The Player %s with Id %d has to bet the small blind because he is the first player of first round!";
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
            }
        }

        //the second player of first round has to raise (preflop)  at least as high as the bigblind or higher
        if (game.getGameRound() == GameRound.Preflop && currentPlayer == activePlayers.get(1)){
            if (action != Action.RAISE || amount != smallBlind){
                String baseErrorMessage = "The Player %s with Id %d has to raise by the amount of the small blind because he is the second player of first round!";
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
            }
        }

        //the third player of first round (preflop) has to call or raise
        if (game.getGameRound() == GameRound.Preflop && currentPlayer == activePlayers.get(2)){
            if (action != Action.CALL && action != Action.RAISE){
                String baseErrorMessage = "The Player %s with Id %d has to call or raise because he is first player";
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
            }
        }



        if (action == Action.BET){
            log.info("bei bet angekommen");
            //betting is only possible if every active player has the same amount in the pot
            for (Player activePlayer : activePlayers) {
                if (currentPlayer.getAmountInPot() != activePlayer.getAmountInPot()){
                    String baseErrorMessage = "The Player %s with Id %d can only call, rais or fold because not every active player has same amount in pot";
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, currentPlayer.getPlayerName(),currentPlayer.getId()));
                }
            }

            //if pot type no limit: maximum bet can be all the credit, player has left. (all in)
            if (game.getPotType().equals("no limit")){
                if (amount>currentPlayer.getCredit()){
                    String baseErrorMessage = "The credit of the Player %s is too low!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
                }
            }
            log.info("no limit gecheckt");
            //if pot type fixed limit in rounds preflop and flop, betted amount must be Lower limit
            //              in other rounds betted amount must be high limit
            //            lower limit = bigblind
            //            higher limit = 2* bigblind
            if (game.getPotType().equals("fixed limit")) {
                int lowerLimit = bigBlind;
                int higherLimit = 2 * bigBlind;

                if (game.getGameRound() == GameRound.Preflop || game.getGameRound() == GameRound.Flop) {
                    if (amount != lowerLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the betted amount in the rounds Preflop and flop must be equal to the lower limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, lowerLimit));
                    }
                }
                else {
                    if (amount != higherLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the betted amount in the rounds Turn Card and River Card must be equal to the higher limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, higherLimit));
                    }
                }
            }
            log.info("fixed limit gecheckt");
            //if pot type pot limit betted amount mustn't be bigger than the amount that is in the pot
            if (game.getPotType().equals("pot limit")) {
                if (amount> pot.getAmount()){
                    String baseErrorMessage = "As the Pot Type is pot limit, the betted amount mustn't be bigger than the amount that is in the pot";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage);
                }
            }
            log.info("pot limit gecheckt");
            //if pot type split limit:  in rounds preflop and flop, betted amount mustn't be higher as Lower limit
            //in other rounds betted amount mustn't be higher than high limit
            //lower limit = bigblind
            //higher limit = 2* bigblind
            if (game.getPotType().equals("split limit")) {
                int lowerLimit = bigBlind;
                int higherLimit = 2 * bigBlind;

                if (game.getGameRound() == GameRound.Preflop || game.getGameRound() == GameRound.Flop) {
                    if (amount > lowerLimit) {
                        String baseErrorMessage = "As the Pot Type is split limit, the betted amount in the rounds Preflop and flop amount mustn't be higher as Lower limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, lowerLimit));
                    }
                }
                else {
                    if (amount > higherLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the betted amount in the rounds Turn Card and River Card mustn't be higher than high limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, higherLimit));
                    }
                }
            }
            log.info("split limit gecheckt");
            //the bet amount mustn't be bigger than the actual credit of the player
            if (amount > currentPlayer.getCredit()) {
                String baseErrorMessage = "A call involves matching the amount already bet. The credit of the Player %s is too low!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }
            log.info("bet amount bigger than actual credit gecheckt");



            currentPlayer.removeCredit(amount);
            currentPlayer.setAmountInPot(currentPlayer.getAmountInPot() + amount);
            pot.addAmount(amount);
            game.setTransactionNr(game.getTransactionNr() + 1);
            gameLog = new GameLog(game.getTransactionNr(),
                    game.getGameRound(),
                    Action.BET,
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
                    0);

            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);
            //return gameLog to GameController
            return gameLog;

        }

        if(action == Action.FOLD){

            game.playerFolds(currentPlayer);

        }
        if(action == Action.RAISE) {
            int addedAmount = (playerWithMostAmountInPot.getAmountInPot() - currentPlayer.getAmountInPot()) + amount;
            int amountToCall = (playerWithMostAmountInPot.getAmountInPot() - currentPlayer.getAmountInPot());

            //if pot type no limit: maximum raise can be all the credit, player has left. (all in)
            if (game.getPotType().equals("no limit")) {
                if (addedAmount > currentPlayer.getCredit()) {
                    String baseErrorMessage = "The credit of the Player %s is too low!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
                }
            }

            //if pot type fixed limit in rounds preflop and flop, raised amount must be Lower limit
            //              in other rounds raised amount must be high limit
            //            lower limit = bigblind
            //            higher limit = 2* bigblind
            //per round it is not possible to raise more than three times
            if (game.getPotType().equals("fixed limit")) {
                int lowerLimit = bigBlind;
                int higherLimit = 2 * bigBlind;

                if (game.getGameRound() == GameRound.Preflop && game.getTimesRaisedPerPreflop() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedPerPreflop() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.Flop && game.getTimesRaisedPerFlop() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedPerFlop() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.TurnCard && game.getTimesRaisedTurnCard() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedTurnCard() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.RiverCard && game.getTimesRaisedRiverCard() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedRiverCard() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.Preflop || game.getGameRound() == GameRound.Flop) {
                    if (amount != lowerLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the raised amount in the rounds Preflop and flop must be equal to the lower limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, lowerLimit));
                    }
                }
                else {
                    if (amount != higherLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the raised amount in the rounds Turn Card and River Card must be equal to the higher limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, higherLimit));
                    }
                }

            }

            //if pot type pot limit raised amount mustn't be bigger than the amount that is in the pot
            if (game.getPotType().equals("pot limit")) {
                if (amount> pot.getAmount()){
                    String baseErrorMessage = "As the Pot Type is pot limit, the raised amount mustn't be bigger than the amount that is in the pot";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage);
                }
            }

            //if pot type split limit:  in rounds preflop and flop, raised amount mustn't be higher as Lower limit
            //in other rounds raised amount mustn't be higher than high limit
            //lower limit = bigblind
            //higher limit = 2* bigblind
            //per round it is not possible to raise more than three times
            if (game.getPotType().equals("split limit")) {
                int lowerLimit = bigBlind;
                int higherLimit = 2 * bigBlind;

                if (game.getGameRound() == GameRound.Preflop && game.getTimesRaisedPerPreflop() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedPerPreflop() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.Flop && game.getTimesRaisedPerFlop() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedPerFlop() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.TurnCard && game.getTimesRaisedTurnCard() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedTurnCard() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }

                if (game.getGameRound() == GameRound.RiverCard && game.getTimesRaisedRiverCard() >= 3) {
                    String baseErrorMessage = "As the Pot Type is fixed limit, it is not possible to raise more than three times per round!";
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
                }
                else {
                    int times = game.getTimesRaisedRiverCard() + 1;
                    game.setTimesRaisedPerPreflop(times);
                }


                if (game.getGameRound() == GameRound.Preflop || game.getGameRound() == GameRound.Flop) {
                    if (amount > lowerLimit) {
                        String baseErrorMessage = "As the Pot Type is split limit, the raised amount in the rounds Preflop and flop amount mustn't be higher as Lower limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, lowerLimit));
                    }
                }
                else {
                    if (amount > higherLimit) {
                        String baseErrorMessage = "As the Pot Type is fixed limit, the raised amount in the rounds Turn Card and River Card mustn't be higher than high limit %D!";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, higherLimit));
                    }
                }
            }

            //the raised amount + the called amount mustn't be bigger than the actual credit of the player
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


            //currentPlayer is only allowed to call if he has less money in the pot as the Player with the most money in the pot.

            if ((playerWithMostAmountInPot.getAmountInPot() <= currentPlayer.getAmountInPot())){
                String baseErrorMessage = "The player &s is not allowed to call because he has more or the same amount of money in the pot as the other players, he has to bet, raise, fold or check!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName()));
            }

            //check if called amount is equal to the difference in the pot between the amounts of the current and the Player with the most money in the pot.
            if (playerWithMostAmountInPot.getAmountInPot() - currentPlayer.getAmountInPot() != amount) {
                String baseErrorMessage = "the amount to call does not match the difference between the amount of Player %s and Player %s in the pot";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, currentPlayer.getPlayerName(), playerWithMostAmountInPot.getPlayerName()));
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
            //option to "check" if no betting action has occurred beforehand.
            // A check simply means to pass the action to the next player in the hand.
            currentPlayer.check();

            // Important: setThisPlayerTurn for all players but nextPlayer to false
            for (Player activePlayer : activePlayers) {
                activePlayer.setThisPlayersTurn(false);
            }
            nextPlayer.setThisPlayersTurn(true);
            //return gameLog to GameController
            //return gameLog;
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
