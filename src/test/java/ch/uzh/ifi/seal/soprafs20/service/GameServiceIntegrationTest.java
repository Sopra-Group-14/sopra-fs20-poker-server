package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.CardAnalyser;
import ch.uzh.ifi.seal.soprafs20.cards.PokerHand;
import ch.uzh.ifi.seal.soprafs20.cards.WinnerCalculator;
import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
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

    @Autowired
    private UserService userService;

    /*@BeforeEach
    public void setup() {

    }*/


    @Test
    public void createGameIsSuccessfulOnValidInput(){

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
    public void calculateWinnerReturnsCorrectWinner(){

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
    public void twoIdenticalHandsCanBeRanked(){

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
    public void isStraightFlush7CardScenarioWorks(){

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
    public void isStraight7CardScenarioWorks(){

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
    public void isThreeOfAKindWorks(){

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
    public void isTwoPairsThirdScenarioWorks(){

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
    public void isHighCardWorks(){

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
    public void AddHostAndAddJoiningUserAddsUsersToPlayersAndActivePlayersLists(){

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


}
