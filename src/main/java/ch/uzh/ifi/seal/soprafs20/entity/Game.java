package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Spectator;

import java.util.List;

/**
 * The game class is responsible for the information regarding the state of a specific game.
 * It has its own instances of objects (e.g. chats, players).
 */
public class Game {

    //Initialize the chats
    private final PlayerChat playerChat = new PlayerChat();
    private final SpectatorChat spectatorChat = new SpectatorChat();
    private long id;

    //Create lists for spectators and players
    private List<Player> players;
    private List<Spectator> spectators;


    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }

    public void addSpectator(Spectator spectator){
        this.spectators.add(spectator);
    }

    public void removeSpectator(Spectator spectator){
        this.spectators.remove(spectator);
    }

    public void setId(long id){this.id = id;}

    public long getId(){return id;}

}
