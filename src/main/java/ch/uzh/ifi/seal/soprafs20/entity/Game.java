package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Pot;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Spectator;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.LinkedList;
import java.util.List;

/**
 * The game class is responsible for the information regarding the state of a specific game.
 * It has its own instances of objects (e.g. chats, players).
 */
public class Game {

    //Define the objects
    //private final GameService gameService;
    private final PlayerChat playerChat = new PlayerChat();
    private final SpectatorChat spectatorChat = new SpectatorChat();
    private long id;
    private int maxPlayers;
    private String gameName, gameHostName;

    private Pot mainPot = new Pot();

    //Create lists for spectators and players
    private List<Player> players = new LinkedList<>();
    private List<Spectator> spectators = new LinkedList<>();

    //Constructor
    public Game(String gameName, String hostName/*, int maxPlayers, GameService gameService*/){
        this.gameName = gameName;
        this.gameHostName = hostName;
        //this.maxPlayers = maxPlayers;
        /*this.gameService = gameService;
        this.id = assignId();*/
    }

    /*private long assignId(){
        return gameService.getNextId();
    }*/


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

    public String getGameName(){return gameName;}

    public String getGameHostName(){return gameHostName;}

    public int getMaxPlayers(){return maxPlayers;}

}
