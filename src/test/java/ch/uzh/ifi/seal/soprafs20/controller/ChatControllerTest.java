package ch.uzh.ifi.seal.soprafs20.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uzh.ifi.seal.soprafs20.chat.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ChatPutDTO;
import ch.uzh.ifi.seal.soprafs20.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        Chat testPlayerChat = new Chat();
        List<ChatLog> testHistory = new LinkedList<>();
        testHistory.add(new ChatLog("11:11:11", "Player1", "userId1", "First Message", "players"));
        testHistory.add(new ChatLog("22:22:22", "Player2", "userId2","Second Message", "players"));
        testHistory.add(new ChatLog("33:33:33", "Player3", "userId3","Third Message", "players"));

        long testGameId = 1L;

        given(chatService.getHistory("players", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/chats/players").header("Authorization", "TestToken");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //chat history get returned
        List<ChatLog> returnedHistory = chatService.getHistory("players", testGameId);

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
        Chat testSpectatorChat = new Chat();
        List<ChatLog> testHistory = new LinkedList<>();
        testHistory.add(new ChatLog("11:11:11", "Player1", "userId1", "First Message", "players"));
        testHistory.add(new ChatLog("22:22:22", "Player2", "userId2","Second Message", "players"));
        testHistory.add(new ChatLog("33:33:33", "Player3", "userId3","Third Message", "players"));


        long testGameId = 1L;

        given(chatService.getHistory("spectators", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/chats/spectators").header("Authorization", "TestToken");

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk());

        //chat history get returned
        List<ChatLog> returnedHistory = chatService.getHistory("spectators", testGameId);

        //assertions
        assertEquals(testHistory.get(0), returnedHistory.get(0));
        assertEquals(testHistory.get(1), returnedHistory.get(1));
        assertEquals(testHistory.get(2), returnedHistory.get(2));

    }

    @Test
    public void sendPlayerChatMessageReturnsCorrectChatlog() throws Exception{

        //given
        //information we give over to the server
        long testGameId = 1L;

        //chatlog we want returned
        ChatPutDTO requestBody = new ChatPutDTO();
        requestBody.setUserId(1L);
        requestBody.setMessage("TestMessage");
        ChatLog testChatLog = new ChatLog("11:11:11", "TestUsername", "userId", "TestMessage", "players");
        List<ChatLog> testHistory = new ArrayList<>();
        testHistory.add(testChatLog);
        given(chatService.getHistory("players", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder putRequest = put("/games/1/chats/players")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody))
                .header("Authorization", "TestToken");

        //then
        mockMvc.perform(putRequest).andExpect(status().isCreated());

        List<ChatLog> returnedChatLog = chatService.getHistory("players", testGameId);

        //assertions
        assertEquals(returnedChatLog.get(0).getMessage(), testChatLog.getMessage());
        assertEquals(returnedChatLog.get(0).getTime(), testChatLog.getTime());
        assertEquals(returnedChatLog.get(0).getUsername(), testChatLog.getUsername());

    }

    @Test
    public void sendSpectatorChatMessageReturnsCorrectChatlog() throws Exception{

        //given
        //information we give over to the server
        long testGameId = 1L;
        //chatlog we want returned
        ChatPutDTO requestBody = new ChatPutDTO();
        requestBody.setUserId(1L);
        requestBody.setMessage("TestMessage");
        ChatLog testChatLog = new ChatLog("11:11:11", "TestUsername","userId", "TestMessage", "spectators");
        List<ChatLog> testHistory = new ArrayList<>();
        testHistory.add(testChatLog);
        given(chatService.getHistory("spectators", testGameId)).willReturn(testHistory);

        //when
        MockHttpServletRequestBuilder putRequest = put("/games/1/chats/spectators")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody))
                .header("Authorization", "TestToken");

        //then
        mockMvc.perform(putRequest).andExpect(status().isCreated());

        List<ChatLog> returnedChatLog = chatService.getHistory("spectators", testGameId);

        //assertions
        assertEquals(returnedChatLog.get(0).getMessage(), testChatLog.getMessage());
        assertEquals(returnedChatLog.get(0).getTime(), testChatLog.getTime());
        assertEquals(returnedChatLog.get(0).getUsername(), testChatLog.getUsername());

    }

    //Test to check whether all messages from player chat occur in spectator chat

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