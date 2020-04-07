package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The GameService takes care of what happens with Games.
 */
@Service
@Transactional
public class GameService {

    private int currentId = 0;

    public static Game createGame(String gameName, long HostId, String potType){
        return null;
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

    public List<Card> getTableCards(long gameId){ return null;}

    public long getNextId(){
        currentId += 1;
        return currentId;
    }

    public void updateBlinds(Game game){}

    public void toggleReadyStatus(long gameId, long playerId){}

    public List<Player> getPlayers(long gameId){return null;}

    public void removePlayer(long gameId, long userId){}

    public List<Game> getAllGames(){return null;}

}
