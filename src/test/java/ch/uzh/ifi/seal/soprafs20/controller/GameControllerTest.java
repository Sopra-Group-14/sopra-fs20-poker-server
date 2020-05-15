package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.constant.Action;
import ch.uzh.ifi.seal.soprafs20.constant.Rank;
import ch.uzh.ifi.seal.soprafs20.constant.Suit;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.GameLog;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameControllerTest
 * This is a WebMvcTest which allows to test the GameController i.e. GET/POST request without actually sending them over the network.
 * This tests if the GameController works.
 */
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    /**
     * When a new game is created, the backend sends back a new game object based on the parameters provided by the frontend.
     * This test checks if the content of the game match the provided ones.
     * @throws Exception
     */
    @Test
    public void returnedCreatedGameIsTheRightOne() throws Exception{

        Game game1 = new Game();
        game1.setGameName("Game1");

        given(gameService.createGame(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).willReturn(game1);

        MockHttpServletRequestBuilder postRequest = post("/games").contentType(MediaType.APPLICATION_JSON).content(asJsonString(game1));

        mockMvc.perform(postRequest).andExpect(status().isCreated());

        Game returnedGame = gameService.createGame("AnyName", 1L, "AnyPot");

        assertEquals(game1.getGameName(), returnedGame.getGameName());

    }

    @Test
    public void retrievingPlayerNamesWorks() throws Exception{

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");

        List<Player> playerList = new LinkedList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);

        Game game = new Game();
        game.setGameLog(new GameLog());
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        given(gameService.getPlayers(Mockito.anyLong())).willReturn(playerList);

        MockHttpServletRequestBuilder getRequest = get("/games/1/players").header("Authorization", "Token");

        mockMvc.perform(getRequest).andExpect(status().isOk());

        List<Player> names = gameService.getPlayers(1L);

        assertEquals(playerList.get(0).getPlayerName(), names.get(0).getPlayerName());
        assertEquals(playerList.get(1).getPlayerName(), names.get(1).getPlayerName());
        assertEquals(playerList.get(2).getPlayerName(), names.get(2).getPlayerName());

    }

   @Test
   @Disabled
    public void getTableCardsReturnsRightCards() throws Exception{

        //given
        //generate card list that gets returned
        List<Card> testTableCardList = new LinkedList<>();
        Card testCard1 = new Card(Suit.CLUBS, Rank.TWO);
        Card testCard2 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card testCard3 = new Card(Suit.SPADES, Rank.FOUR);
        testTableCardList.add(testCard1);
        testTableCardList.add(testCard2);
        testTableCardList.add(testCard3);

        //generate card list we want returned
        List<Card> testTableCardList2 = new LinkedList<>();
        Card testCard11 = new Card(Suit.CLUBS, Rank.TWO);
        Card testCard21 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card testCard31 = new Card(Suit.SPADES, Rank.FOUR);
        testTableCardList2.add(testCard11);
        testTableCardList2.add(testCard21);
        testTableCardList2.add(testCard31);

        long testGameId = 1L;

        //given(gameService.getTableCards(Mockito.anyLong())).willReturn(testTableCardList);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/table");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //returned card list
        /*List<Card> returnedCardList = gameService.getTableCards(testGameId);

        //assertions
        assertEquals(testTableCardList2.get(0).getSuit(), returnedCardList.get(0).getSuit());
        assertEquals(testTableCardList2.get(0).getRank(), returnedCardList.get(0).getRank());
        assertEquals(testTableCardList2.get(1).getSuit(), returnedCardList.get(1).getSuit());
        assertEquals(testTableCardList2.get(1).getRank(), returnedCardList.get(1).getRank());
        assertEquals(testTableCardList2.get(2).getSuit(), returnedCardList.get(2).getSuit());
        assertEquals(testTableCardList2.get(2).getRank(), returnedCardList.get(2).getRank());*/

    }

   @Test
   public void retrievingGamesGetsAllGames() throws Exception{

       Game game1 = new Game();
       Game game2 = new Game();
       game1.setGameName("Game1");
       game2.setGameName("Game2");

       List<Game> testGameList = new LinkedList<>();
       testGameList.add(game1);
       testGameList.add(game2);

       given(gameService.getAllGames()).willReturn(testGameList);

       MockHttpServletRequestBuilder getRequest = get("/games");

       mockMvc.perform(getRequest).andExpect(status().isOk());

       List<Game> returnedGameList = gameService.getAllGames();

       assertEquals(returnedGameList.get(0).getGameName(), testGameList.get(0).getGameName());
       assertEquals(returnedGameList.get(1).getGameName(), testGameList.get(1).getGameName());

   }

   @Test
   public void getGameLogReturnsRightParameters() throws Exception{

       //We set up a testGameLog that we want returned
       GameLog testGameLog = new GameLog();
       Long testId = 1L;

       List<Action> actionList = new LinkedList<>();
       actionList.add(Action.BET);
       List<Card> revealedCards = new LinkedList<>();
       revealedCards.add(new Card(Suit.CLUBS, Rank.EIGHT));

       testGameLog.setPossibleActions(actionList);
       testGameLog.setRevealedCards(revealedCards);
       testGameLog.setGameName("TestGameName");

       //given
       given(gameService.getGameLog(Mockito.anyLong())).willReturn(testGameLog);

       //when
       MockHttpServletRequestBuilder getRequest = get("/games/1");

       //then
       mockMvc.perform(getRequest).andExpect(status().isOk());

       //returned gameLog
       GameLog returnedGameLog = gameService.getGameLog(testId);

       //assertions
       assertEquals(testGameLog.getPossibleActions(), returnedGameLog.getPossibleActions());
       assertEquals(testGameLog.getRevealedCards(), returnedGameLog.getRevealedCards());
       assertEquals(testGameLog.getGameName(), returnedGameLog.getGameName());

    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new Error(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}


