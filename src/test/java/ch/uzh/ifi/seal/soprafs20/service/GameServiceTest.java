package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class GameServiceTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        while(gameService.getAllGames().size() > 0){
            gameService.getAllGames().remove(0);
        }
    }

    @Test
    public void addJoiningPlayerAddsCorrectPlayer() {

        //given
        Game testGame = gameService.createGame("TestGameName", -1L, "Fixed");
        Player testPlayer1 = new Player("TestPlayerOne");
        Player testPlayer2 = new Player("TestPlayerTwo");
        Player testPlayer3 = new Player("TestPlayerThree");
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);

        // when
        Player returnedPlayer1 = testGame.getPlayers().get(0);
        Player returnedPlayer2 = testGame.getPlayers().get(1);
        Player returnedPlayer3 = testGame.getPlayers().get(2);

        // then
        assertEquals(testPlayer1.getPlayerName(), returnedPlayer1.getPlayerName());
        assertEquals(testPlayer2.getPlayerName(), returnedPlayer2.getPlayerName());
        assertEquals(testPlayer3.getPlayerName(), returnedPlayer3.getPlayerName());

    }

    @Test
    public void playGameEndsGameIfOnlyOnePlayer(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User hostUser = userService.createUser(toCreateUser);

        Game game = gameService.createGame("Game", hostUser.getId(), "Fixed");

        game.playGame(Action.BET, hostUser.getId());

    }

    @Test
    public void playGameEndsGameIfOnlyOnePlayerHasCredit(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User toCreateUser2 = new User();
        toCreateUser2.setName("Name2");
        toCreateUser2.setPassword("Password2");
        toCreateUser2.setUsername("Username2");

        User hostUser = userService.createUser(toCreateUser);
        User joiningUser = userService.createUser(toCreateUser2);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());

        Long hostId = hostUser.getId();
        Long joinId = joiningUser.getId();
        Long gameId = game.getGameId();

        int credit = game.getPlayerById(hostId).getCredit();
        game.getPlayerById(hostId).removeCredit(credit);

        game.playGame(Action.CALL, joinId);

    }

    @Test
    public void testRemovePlayerAndRemoveActivePlayer(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User toCreateUser2 = new User();
        toCreateUser2.setName("Name2");
        toCreateUser2.setPassword("Password2");
        toCreateUser2.setUsername("Username2");

        User hostUser = userService.createUser(toCreateUser);
        User joiningUser = userService.createUser(toCreateUser2);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());

        Long hostId = hostUser.getId();
        Long joinId = joiningUser.getId();

        Player host = game.getPlayerById(hostId);
        Player join = game.getPlayerById(joinId);

        game.removeActivePlayer(host);
        assertEquals(game.getActivePlayers().get(0), join);
        game.removePlayer(join);
        assertEquals(game.getPlayers().get(0), host);

    }

    @Test
    public void addTableCardsAddsATableCard(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User hostUser = userService.createUser(toCreateUser);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);

        game.addTableCard();

        List<Card> tableCards = game.getTableCards();

        assertNotNull(tableCards.get(0));

    }

    @Test
    public void nullGameLogReturnsRightNullGameLog(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User hostUser = userService.createUser(toCreateUser);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);

        GameLog nullGameLog = game.nullGameLog();

        assertEquals(nullGameLog.getAction(), Action.NONE);
        assertEquals(nullGameLog.getGameName(), game.getGameName());

    }

    @Test
    public void testRemovingSingleOrAllGamesFromGameSelect(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User hostUser = userService.createUser(toCreateUser);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");
        Game game2 = gameService.createGame("Game2", hostUser.getId(), "None");
        Game game3 = gameService.createGame("Game3", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addHost(hostUser.getId(), game2);
        gameService.addHost(hostUser.getId(), game3);

        GameSelect gameSelect = new GameSelect();

        gameSelect.addGame(game);
        gameSelect.addGame(game2);
        gameSelect.addGame(game3);

        List<Game> games = gameSelect.getAllGames();

        assertEquals(games.get(0), game);
        assertEquals(games.get(1), game2);
        assertEquals(games.get(2), game3);

        gameSelect.removeGame(game2);

        assertEquals(games.get(0), game);
        assertEquals(games.get(1), game3);

        gameSelect.removeAllGames();

        Game game4 = gameService.createGame("Game4", hostUser.getId(), "None");
        gameSelect.addGame(game4);

        assertEquals(gameSelect.getAllGames().get(0), game4);

    }

    @Test
    public void testGetGameByToken(){

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("Username");

        User hostUser = userService.createUser(toCreateUser);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);

        GameSelect gameSelect = new GameSelect();

        gameSelect.addGame(game);

        String token = game.getHostToken();

        assertEquals(game, gameSelect.getGameByToken(token));

    }

}

