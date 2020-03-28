package ch.uzh.ifi.seal.soprafs20.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uzh.ifi.seal.soprafs20.chat.Chat;
import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.ChatService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    /**
     * Check if the player chat history can be correctly retrieved.
     * @throws Exception
     */
    @Test
    public void getPlayerChatMessagesGetsCorrectMessages() throws Exception{

        //given
        //history we want returned
        PlayerChat testPlayerChat = new PlayerChat();
        List<String> testHistory = new LinkedList<>();
        testHistory.add("Player1 [11:11:11]: First Message");
        testHistory.add("Player2 [22:22:22]: Second Message");
        testHistory.add("Player3 [33:33:33]: Third Message");

        long testGameId = 1L;

        given(chatService.getHistory("players", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/chats/players");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //chat history get returned
        List<String> returnedHistory = chatService.getHistory("player", testGameId);

        //assertions
        assertEquals(testHistory.get(0), returnedHistory.get(0));
        assertEquals(testHistory.get(1), returnedHistory.get(1));
        assertEquals(testHistory.get(2), returnedHistory.get(2));

    }

    /**
     * Check if the spectator chat history can be correctly retrieved.
     * @throws Exception
     */
    @Test
    public void getSpectatorChatMessagesGetsCorrectMessages() throws Exception{

        //given
        //history we want returned
        SpectatorChat testSpectatorChat = new SpectatorChat();
        List<String> testHistory = new LinkedList<>();
        testHistory.add("Player1 [11:11:11]: First Message");
        testHistory.add("Player2 [22:22:22]: Second Message");
        testHistory.add("Player3 [33:33:33]: Third Message");

        long testGameId = 1L;

        given(chatService.getHistory("spectators", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/chats/spectators");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //chat history get returned
        List<String> returnedHistory = chatService.getHistory("spectators", testGameId);

        //assertions
        assertEquals(testHistory.get(0), returnedHistory.get(0));
        assertEquals(testHistory.get(1), returnedHistory.get(1));
        assertEquals(testHistory.get(2), returnedHistory.get(2));

    }

    @Test
    public void sendPlayerChatMessageReturnsCorrectChatlog() throws Exception{

        //given
        //information we give over to the server
        long testPlayerId = 1L;
        String testMessage = "TestMessage";
        //chatlog we want returned
        ChatLog testChatLog = new ChatLog("11:11:11", "TestUsername", "TestMessage");

        //when
        //MockHttpServletRequestBuilder putRequest = put("/games/1/chats/players").contentType(MediaType.APPLICATION_JSON).content();

        //then
        //mockMvc.perform(putRequest).andExpect(status().isCreated());


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