package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import java.util.List;

/**
 * The game class is responsible for the information regarding the state of a specific game.
 * It has its own instances of objects (e.g. chats, players).
 */
public class Game {

    //Initialize the chats
    private final PlayerChat playerChat = new PlayerChat();
    private final SpectatorChat spectatorChat = new SpectatorChat();

    //Create lists for spectators and players
    private List<Player> players;
    private List<Spectator> spectators;


    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void addSpectator(Spectator spectator){
        spectators.add(spectator);
    }

    public void removeSpectator(Spectator spectator){
        spectators.remove(spectator);
    }

}
