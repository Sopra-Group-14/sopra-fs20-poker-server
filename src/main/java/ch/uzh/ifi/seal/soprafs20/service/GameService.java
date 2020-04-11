package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.NumberUp;
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
        if(action == Action.FOLD){

        }
        if(action == Action.RAISE){

        }
        if(action == Action.CALL){

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

    public void updateBlinds(Game game){}

    public void toggleReadyStatus(long gameId, long playerId){}

    public List<Player> getPlayers(long gameId){
        return gameSelect.getGameById(gameId).getPlayers();
    }

    public void removePlayer(long gameId, long userId){}

    public List<Game> getAllGames(){return gameSelect.getAllGames();}

}
