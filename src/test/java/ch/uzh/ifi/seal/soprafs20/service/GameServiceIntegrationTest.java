package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
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

    /*@BeforeEach
    public void setup() {
        gameSelect.removeAllGames();
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
        List<Player> players = new LinkedList<>();

        players.add(player1);
        //players.add(player2);
        //players.add(player3);

        player1.addToHand(new Card(Suit.CLUBS, Rank.ACE));
        player1.addToHand(new Card(Suit.CLUBS, Rank.SEVEN));

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.DIAMONDS, Rank.THREE));
        cards.add(new Card(Suit.SPADES, Rank.KING));
        cards.add(new Card(Suit.DIAMONDS, Rank.TWO));
        cards.add(new Card(Suit.SPADES, Rank.SIX));
        cards.add(new Card(Suit.CLUBS, Rank.NINE));

        List<Player> winners = winnerCalculator.isWinner(players, cards);
        Player winner = winners.get(0);

        assertEquals(player1.getPlayerName(), winner.getPlayerName());

    }


}
