package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.CardAnalyser;
import ch.uzh.ifi.seal.soprafs20.cards.PokerHand;
import ch.uzh.ifi.seal.soprafs20.cards.WinnerCalculator;
import ch.uzh.ifi.seal.soprafs20.constant.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    //@Qualifier("gameSelect")
    //@Autowired
    /*@MockBean
    private GameSelect gameSelect;*/

    @Autowired
    private GameService gameService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        while (gameService.getAllGames().size() > 0) {
            gameService.getAllGames().remove(0);
        }
    }

    @Test
    public void createGameIsSuccessfulOnValidInput() {

        //given
        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");

        //when
        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());

        //then
        assertEquals(testGame.getGameName(), createdGame.getGameName());
        assertEquals(testGame.getGameHostID(), createdGame.getGameHostID());
        assertEquals(testGame.getPotType(), createdGame.getPotType());

    }

    @Test
    public void calculateWinnerReturnsCorrectWinner() {

        WinnerCalculator winnerCalculator = new WinnerCalculator();

        Player player1 = new Player("One");
        Player player2 = new Player("Two");
        Player player3 = new Player("Three");
        Player player4 = new Player("Three");
        List<Player> players = new LinkedList<>();

        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        player1.addToHand(new Card(Suit.CLUBS, Rank.ACE));
        player1.addToHand(new Card(Suit.CLUBS, Rank.SEVEN));

        player2.addToHand(new Card(Suit.DIAMONDS, Rank.KING));
        player2.addToHand(new Card(Suit.DIAMONDS, Rank.QUEEN));

        player3.addToHand(new Card(Suit.DIAMONDS, Rank.NINE));
        player3.addToHand(new Card(Suit.DIAMONDS, Rank.QUEEN));

        player4.addToHand(new Card(Suit.DIAMONDS, Rank.TWO));
        player4.addToHand(new Card(Suit.DIAMONDS, Rank.THREE));

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.DIAMONDS, Rank.ACE));
        cards.add(new Card(Suit.DIAMONDS, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.JACK));
        cards.add(new Card(Suit.SPADES, Rank.SIX));
        cards.add(new Card(Suit.DIAMONDS, Rank.TEN));

        List<Player> winners = winnerCalculator.isWinner(players, cards);
        Player winner = winners.get(0);

        assertEquals(player2.getPlayerName(), winner.getPlayerName());

    }

    @Test
    public void twoIdenticalHandsCanBeRanked() {

        WinnerCalculator winnerCalculator = new WinnerCalculator();

        Player player1 = new Player("One");
        Player player2 = new Player("Two");
        List<Player> players = new LinkedList<>();

        players.add(player1);
        players.add(player2);

        player1.addToHand(new Card(Suit.SPADES, Rank.ACE));
        player1.addToHand(new Card(Suit.CLUBS, Rank.KING));

        player2.addToHand(new Card(Suit.HEARTS, Rank.ACE));
        player2.addToHand(new Card(Suit.HEARTS, Rank.SIX));


        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.DIAMONDS, Rank.ACE));
        cards.add(new Card(Suit.DIAMONDS, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.JACK));
        cards.add(new Card(Suit.SPADES, Rank.SIX));
        cards.add(new Card(Suit.DIAMONDS, Rank.TEN));

        List<Player> winners = winnerCalculator.isWinner(players, cards);
        Player winner = winners.get(0);

        assertEquals(player1.getPlayerName(), winner.getPlayerName());

    }

    @Test
    public void isStraightFlush7CardScenarioWorks() {

        CardAnalyser cardAnalyser = new CardAnalyser();

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.DIAMONDS, Rank.SEVEN));
        cards.add(new Card(Suit.DIAMONDS, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
        cards.add(new Card(Suit.DIAMONDS, Rank.JACK));
        cards.add(new Card(Suit.DIAMONDS, Rank.NINE));
        cards.add(new Card(Suit.DIAMONDS, Rank.TEN));

        PokerHand hand = cardAnalyser.getPokerHand(cards);

        assertEquals(hand.getComboValue(), 9);
        assertEquals(hand.getRankList().get(0), Rank.KING);

    }

    @Test
    public void isStraight7CardScenarioWorks() {

        CardAnalyser cardAnalyser = new CardAnalyser();

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        cards.add(new Card(Suit.SPADES, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.EIGHT));
        cards.add(new Card(Suit.DIAMONDS, Rank.JACK));
        cards.add(new Card(Suit.HEARTS, Rank.NINE));
        cards.add(new Card(Suit.SPADES, Rank.TEN));

        PokerHand hand = cardAnalyser.getPokerHand(cards);

        assertEquals(hand.getComboValue(), 5);
        assertEquals(hand.getRankList().get(0), Rank.KING);

    }

    @Test
    public void isThreeOfAKindWorks() {

        CardAnalyser cardAnalyser = new CardAnalyser();

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        cards.add(new Card(Suit.SPADES, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.SIX));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.FIVE));
        cards.add(new Card(Suit.SPADES, Rank.QUEEN));

        PokerHand hand = cardAnalyser.getPokerHand(cards);

        assertEquals(hand.getComboValue(), 4);
        assertEquals(hand.getRankList().get(0), Rank.QUEEN);
        assertEquals(hand.getRankList().get(1), Rank.KING);
        assertEquals(hand.getRankList().get(2), Rank.SEVEN);

    }

    @Test
    public void isTwoPairsThirdScenarioWorks() {

        CardAnalyser cardAnalyser = new CardAnalyser();

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        cards.add(new Card(Suit.SPADES, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.SIX));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.FIVE));
        cards.add(new Card(Suit.SPADES, Rank.FIVE));

        PokerHand hand = cardAnalyser.getPokerHand(cards);

        assertEquals(hand.getComboValue(), 3);
        assertEquals(hand.getRankList().get(0), Rank.QUEEN);
        assertEquals(hand.getRankList().get(1), Rank.FIVE);
        assertEquals(hand.getRankList().get(2), Rank.KING);

    }

    @Test
    public void isHighCardWorks() {

        CardAnalyser cardAnalyser = new CardAnalyser();

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        cards.add(new Card(Suit.SPADES, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.ACE));
        cards.add(new Card(Suit.HEARTS, Rank.SIX));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cards.add(new Card(Suit.HEARTS, Rank.TWO));
        cards.add(new Card(Suit.SPADES, Rank.FIVE));

        PokerHand hand = cardAnalyser.getPokerHand(cards);

        assertEquals(hand.getComboValue(), 1);
        assertEquals(hand.getRankList().get(0), Rank.ACE);
        assertEquals(hand.getRankList().get(1), Rank.KING);
        assertEquals(hand.getRankList().get(2), Rank.QUEEN);
        assertEquals(hand.getRankList().get(3), Rank.SEVEN);
        assertEquals(hand.getRankList().get(4), Rank.SIX);

    }

    @Test
    public void AddHostAndAddJoiningUserAddsUsersToPlayersAndActivePlayersLists() {

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

        Game game = gameService.createGame("Game", hostUser.getId(), "Fixed");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());

        List<Player> players = gameService.getPlayers(game.getGameId());
        List<Player> activePlayers = gameService.getGame(game.getGameId()).getActivePlayers();

        assertEquals(hostUser.getUsername(), players.get(0).getPlayerName());
        assertEquals(joiningUser.getUsername(), players.get(1).getPlayerName());
        assertEquals(hostUser.getUsername(), activePlayers.get(0).getPlayerName());
        assertEquals(joiningUser.getUsername(), activePlayers.get(1).getPlayerName());

    }

    @Test
    public void testActionBet() {

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

        //Normal Case
        GameLog gameLog = gameService.executeAction(Action.BET, 10, gameId, hostId);

        assertEquals(gameLog.getAction(), Action.BET);

    }

    @Test
    public void testActionFold() {

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

        //Normal case
        GameLog gameLog = gameService.executeAction(Action.FOLD, 0, gameId, hostId);

        assertEquals(gameLog.getAction(), Action.FOLD);
        assertEquals(game.getActivePlayers().get(0).getPlayerName(), joiningUser.getUsername());

    }

    @Test
    public void testActionRaise() {

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

        //Normal case
        GameLog gameLog = gameService.executeAction(Action.RAISE, 10, gameId, hostId);

        assertEquals(gameLog.getAction(), Action.RAISE);
        assertEquals(gameLog.getRaiseAmount(), 10);

    }

    @Test
    public void testActionCall() {

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

        //Normal case
        GameLog gameLog = gameService.executeAction(Action.CALL, 0, gameId, hostId);

        assertEquals(gameLog.getAction(), Action.CALL);

    }

    @Test
    public void testActionCheck() {

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

        //Normal case
        GameLog gameLog = gameService.executeAction(Action.CHECK, 0, gameId, hostId);

        assertEquals(gameLog.getAction(), Action.CHECK);

    }

    @Test
    public void testDifferentPotTypes() {

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

        //No Limit
        Game game = gameService.createGame("Game", hostUser.getId(), "no limit");
        //Pot Limit
        Game game2 = gameService.createGame("Game2", hostUser.getId(), "pot limit");
        //Fixed Limit
        Game game3 = gameService.createGame("Game3", hostUser.getId(), "fixed limit");
        //Split Limit
        Game game4 = gameService.createGame("Game4", hostUser.getId(), "split limit");

        Long hostId = hostUser.getId();
        Long joinId = joiningUser.getId();
        Long gameId = game.getGameId();
        Long gameId2 = game2.getGameId();
        Long gameId3 = game3.getGameId();
        Long gameId4 = game4.getGameId();

        gameService.addHost(hostId, game);
        gameService.addJoiningPlayer(joinId, gameId);
        gameService.addHost(hostId, game2);
        gameService.addJoiningPlayer(joinId, gameId2);
        gameService.addHost(hostId, game3);
        gameService.addJoiningPlayer(joinId, gameId3);
        gameService.addHost(hostId, game4);
        gameService.addJoiningPlayer(joinId, gameId4);

        game.startGame();
        game2.startGame();
        game3.startGame();
        game4.startGame();

        //Execute the example game flow for all games
        GameLog gameLoga = gameService.executeAction(Action.BET, 5, gameId, hostId);
        assertEquals(gameLoga.getAction(), Action.BET);
        assertEquals(gameLoga.getRaiseAmount(), 5);
        GameLog gameLogb = gameService.executeAction(Action.RAISE, 5, gameId, joinId);
        assertEquals(gameLogb.getAction(), Action.RAISE);
        assertEquals(gameLoga.getRaiseAmount(), 5);
        GameLog gameLogc = gameService.executeAction(Action.CALL, 0, gameId, hostId);
        assertEquals(gameLogc.getAction(), Action.CALL);
        GameLog gameLogd = gameService.executeAction(Action.CHECK, 0, gameId, joinId);
        assertEquals(gameLogd.getAction(), Action.CHECK);
        GameLog gameLoge = gameService.executeAction(Action.FOLD, 0, gameId, hostId);
        assertEquals(gameLoge.getAction(), Action.FOLD);
        assertEquals(game.getActivePlayers().get(0).getPlayerName(), joiningUser.getUsername());

        GameLog gameLog2a = gameService.executeAction(Action.BET, 5, gameId2, hostId);
        assertEquals(gameLog2a.getAction(), Action.BET);
        assertEquals(gameLog2a.getRaiseAmount(), 5);
        GameLog gameLog2b = gameService.executeAction(Action.RAISE, 5, gameId2, joinId);
        assertEquals(gameLog2b.getAction(), Action.RAISE);
        assertEquals(gameLog2b.getRaiseAmount(), 5);
        GameLog gameLog2c = gameService.executeAction(Action.CALL, 0, gameId2, hostId);
        assertEquals(gameLog2c.getAction(), Action.CALL);
        GameLog gameLog2d = gameService.executeAction(Action.CHECK, 0, gameId2, joinId);
        assertEquals(gameLog2d.getAction(), Action.CHECK);
        GameLog gameLog2e = gameService.executeAction(Action.FOLD, 0, gameId2, hostId);
        assertEquals(gameLog2e.getAction(), Action.FOLD);
        assertEquals(game2.getActivePlayers().get(0).getPlayerName(), joiningUser.getUsername());

        GameLog gameLog3a = gameService.executeAction(Action.BET, 5, gameId3, hostId);
        assertEquals(gameLog3a.getAction(), Action.BET);
        assertEquals(gameLog3a.getRaiseAmount(), 5);
        GameLog gameLog3b = gameService.executeAction(Action.RAISE, 5, gameId3, joinId);
        assertEquals(gameLog3b.getAction(), Action.RAISE);
        assertEquals(gameLog3b.getRaiseAmount(), 5);
        GameLog gameLog3c = gameService.executeAction(Action.CALL, 0, gameId3, hostId);
        assertEquals(gameLog3c.getAction(), Action.CALL);
        GameLog gameLog3d = gameService.executeAction(Action.CHECK, 0, gameId3, joinId);
        assertEquals(gameLog3d.getAction(), Action.CHECK);
        GameLog gameLog3e = gameService.executeAction(Action.FOLD, 0, gameId3, hostId);
        assertEquals(gameLog3e.getAction(), Action.FOLD);
        assertEquals(game3.getActivePlayers().get(0).getPlayerName(), joiningUser.getUsername());

        GameLog gameLog4a = gameService.executeAction(Action.BET, 5, gameId4, hostId);
        assertEquals(gameLog4a.getAction(), Action.BET);
        assertEquals(gameLog4a.getRaiseAmount(), 5);
        GameLog gameLog4b = gameService.executeAction(Action.RAISE, 5, gameId4, joinId);
        assertEquals(gameLog4b.getAction(), Action.RAISE);
        assertEquals(gameLog4b.getRaiseAmount(), 5);
        GameLog gameLog4c = gameService.executeAction(Action.CALL, 0, gameId4, hostId);
        assertEquals(gameLog4c.getAction(), Action.CALL);
        GameLog gameLog4d = gameService.executeAction(Action.CHECK, 0, gameId4, joinId);
        assertEquals(gameLog4d.getAction(), Action.CHECK);
        GameLog gameLog4e = gameService.executeAction(Action.FOLD, 0, gameId4, hostId);
        assertEquals(gameLog4e.getAction(), Action.FOLD);
        assertEquals(game4.getActivePlayers().get(0).getPlayerName(), joiningUser.getUsername());

    }

    @Test
    public void testRoundStart() {

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

        GameLog gameLog = gameService.roundStart(game);

        assertEquals(gameLog.getGameRound(), GameRound.Preflop);
        assertEquals(gameLog.getAction(), Action.BET);
        assertFalse(gameLog.getThisPlayersTurn());
        assertTrue(gameLog.getNextPlayersTurn());

    }

    @Test
    public void removePlayerTest() {

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("host");

        User toCreateUser2 = new User();
        toCreateUser2.setName("Name2");
        toCreateUser2.setPassword("Password2");
        toCreateUser2.setUsername("join1");

        User toCreateUser3 = new User();
        toCreateUser3.setName("Name3");
        toCreateUser3.setPassword("Password3");
        toCreateUser3.setUsername("join2");

        User hostUser = userService.createUser(toCreateUser);
        User joiningUser = userService.createUser(toCreateUser2);
        User joiningUser2 = userService.createUser(toCreateUser3);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());
        gameService.addJoiningPlayer(joiningUser2.getId(), game.getGameId());

        GameLog gameLog = gameService.roundStart(game);

        gameService.removePlayer(game.getGameId(), joiningUser2.getId());

        assertEquals(2, gameLog.getActivePlayers().size());
        assertEquals(2, gameLog.getPlayers().size());
    }


    @Test
    public void playerLeaves_onePlayerLeftInGame() {

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("host");

        User toCreateUser2 = new User();
        toCreateUser2.setName("Name2");
        toCreateUser2.setPassword("Password2");
        toCreateUser2.setUsername("join1");

        User hostUser = userService.createUser(toCreateUser);
        User joiningUser = userService.createUser(toCreateUser2);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());

        GameLog gameLog = gameService.roundStart(game);

        gameService.removePlayer(game.getGameId(), joiningUser.getId());

        assertEquals(true, game.getGameLog().getGameOver());
        assertEquals(true, game.getGameLog().getRoundOver());

    }

    @Test
    public void playerLeaves_onePlayerLeftInRound() {

        User toCreateUser = new User();
        toCreateUser.setName("Name");
        toCreateUser.setPassword("Password");
        toCreateUser.setUsername("host");

        User toCreateUser2 = new User();
        toCreateUser2.setName("Name2");
        toCreateUser2.setPassword("Password2");
        toCreateUser2.setUsername("join1");

        User toCreateUser3 = new User();
        toCreateUser3.setName("Name3");
        toCreateUser3.setPassword("Password3");
        toCreateUser3.setUsername("join2");

        User hostUser = userService.createUser(toCreateUser);
        User joiningUser = userService.createUser(toCreateUser2);
        User joiningUser2 = userService.createUser(toCreateUser3);

        Game game = gameService.createGame("Game", hostUser.getId(), "None");

        gameService.addHost(hostUser.getId(), game);
        gameService.addJoiningPlayer(joiningUser.getId(), game.getGameId());
        gameService.addJoiningPlayer(joiningUser2.getId(), game.getGameId());

        game.removeActivePlayer(game.getPlayerById(2L));

        gameService.removePlayer(game.getGameId(), joiningUser2.getId());


        /*

        both assertion expect false as the method game.removePlayer starts a new round if there is only one active player
        left once it has been called

         */

        assertEquals(false, game.getGameLog().getGameOver());
        assertEquals(false, game.getGameLog().getRoundOver());
        assertEquals(2, game.getPlayers().size());

    }

}
