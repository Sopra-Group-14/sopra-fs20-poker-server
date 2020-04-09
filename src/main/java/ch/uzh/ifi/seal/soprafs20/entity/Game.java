package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Pot;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Spectator;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
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
    private long gameId;
    private int maxPlayers;
    private String gameName, gameHostName;

    private Pot mainPot = new Pot();
    private Deck deck = new Deck();

    //Create lists for spectators, players and the active players
    private List<Player> players = new LinkedList<>();
    private List<Spectator> spectators = new LinkedList<>();
    private List<Player> activePlayers = new LinkedList<>();

    //Create a list for the cards on the table
    private List<Card> tableCards = new LinkedList<>();

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

    public List<Player> getPlayers(){return players;}

    public void addSpectator(Spectator spectator){
        this.spectators.add(spectator);
    }

    public void removeSpectator(Spectator spectator){
        this.spectators.remove(spectator);
    }

    public void setGameId(long id){this.gameId = id;}

    public long getGameId(){return gameId;}

    public String getGameName(){return gameName;}

    public String getGameHostName(){return gameHostName;}

    public int getMaxPlayers(){return maxPlayers;}

    public void updateBlinds(){}

    public List<Card> getTableCards(){
        return new LinkedList<>(tableCards);
    }

    public void addTableCard(){
        if (tableCards.size()>2){
            throw new SopraServiceException("There are already three cards on the table in the game");
        }
        deck.shuffle();
        tableCards.add(deck.getTopCard());
    }

    public void removeTableCards(){
        tableCards = new LinkedList<>();
    }

}

