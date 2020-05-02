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

    /**
     * When a new game is created, the backend sends back a new game object based on the parameters provided by the frontend.
     * This test checks if the content of the game match the provided ones.
     * @throws Exception
     */
    /*@Test
    public void returnedCreatedGameIsTheRightOne() throws Exception{

        //givenString gameName, long HostId, String potType
        //The game we post
        Game postGame = gameService.createGame("TestGame", 1L, "None");
        //The game we want to get back
        Game testGame = gameService.createGame("TestGame", 1L, "None");

        given(gameService.createGame("TestGame", 1L, "None")).willReturn(testGame);

        //when
        MockHttpServletRequestBuilder postRequest = post("/games").contentType(MediaType.APPLICATION_JSON).content(asJsonString(postGame));

        //then
        mockMvc.perform(postRequest).andExpect(status().isCreated());

        //Game object we want returned
        Game returnedGame = gameService.createGame("TestGame", 1L, "None");

        //Comparing what we want with what we actually get returned
        //assertEquals(returnedGame.getGameId(), testGame.getGameId());
        assertEquals(returnedGame.getGameName(), testGame.getGameName());

    }*/

   /* @Test
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

        given(gameService.getTableCards(Mockito.anyLong())).willReturn(testTableCardList);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/table");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //returned card list
        List<Card> returnedCardList = gameService.getTableCards(testGameId);

        //assertions
        assertEquals(testTableCardList2.get(0).getSuit(), returnedCardList.get(0).getSuit());
        assertEquals(testTableCardList2.get(0).getRank(), returnedCardList.get(0).getRank());
        assertEquals(testTableCardList2.get(1).getSuit(), returnedCardList.get(1).getSuit());
        assertEquals(testTableCardList2.get(1).getRank(), returnedCardList.get(1).getRank());
        assertEquals(testTableCardList2.get(2).getSuit(), returnedCardList.get(2).getSuit());
        assertEquals(testTableCardList2.get(2).getRank(), returnedCardList.get(2).getRank());

    }*/

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


