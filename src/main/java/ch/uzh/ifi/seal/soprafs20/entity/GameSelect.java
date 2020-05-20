package ch.uzh.ifi.seal.soprafs20.entity;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all currently existing games.
 * New games can be added, finished ones removed and a list of all held games can be retrieved.
 */
public class GameSelect {

    private List<Game> gameList = new ArrayList<>();
    public static final Game NULL_GAME = new Game("nullGame");

    public void addGame(Game game){
        if(!gameList.contains(game)) {
            gameList.add(game);
        }
    }

    public void removeGame(Game game){
        gameList.remove(game);
    }

    public void removeAllGames(){
        while(gameList.size()>0){
            gameList.remove(0);
        }
    }

    public List<Game> getAllGames(){
        return gameList;
    }

    //Scan the gameList until the game with gameId is found. If none is found, null is returned.
    public Game getGameById(long gameId){
        int i;
        Game returnGame = NULL_GAME;

        if(gameList != null) {
            for (i = 0; i < gameList.size(); i++) {
                if (gameList.get(i).getGameId() == gameId) {
                    returnGame = gameList.get(i);
                    break;
                }
            }
        }
        return returnGame;
    }

    //Scan the gameList until the game with gameId is found. If none is found, null is returned.
    public Game getGameByToken(String token){
        int i;
        Game returnGame = NULL_GAME;
        for(i=0;i<gameList.size();i++){
            if(gameList.get(i).getHostToken().equals(token)){
                returnGame = gameList.get(i);
                break;
            }
        }
        return returnGame;
    }

}
