package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;

/**
 * The GameService takes care of what happens with Games.
 */
public class GameService {

    private int currentId = 0;

    public Game createGame(Game game){
        return null;
    }

    public GameLog executeAction(Action action, int amount, long gameId, long playerId, String token){
        return null;
    }

    public long getNextId(){
        currentId += 1;
        return currentId;
    }

    public void updateBlinds(Game game){}

}
