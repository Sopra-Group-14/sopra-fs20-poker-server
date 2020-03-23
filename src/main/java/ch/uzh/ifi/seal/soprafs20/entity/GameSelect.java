package ch.uzh.ifi.seal.soprafs20.entity;
import java.util.List;

/**
 * This class holds all currently existing games.
 * New games can be added, finished ones removed and a list of all held games can be retrieved.
 */
public class GameSelect {

    private List<Game> gameList;


    public void addGame(Game game){
        gameList.add(game);
    }

    public void removeGame(Game game){
        gameList.remove(game);
    }

    public List<Game> getAllGames(){
        return gameList;
    }

}
