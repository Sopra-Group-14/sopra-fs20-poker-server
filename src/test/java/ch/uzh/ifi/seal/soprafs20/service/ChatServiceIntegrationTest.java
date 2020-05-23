package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ChatPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for the UserResource REST resource.
 *
 * @see ChatService
 */

@WebAppConfiguration
@SpringBootTest
public class ChatServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ChatService chatService;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    List<ChatLog> chat = new ArrayList<>();

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        while (gameService.getAllGames().size() > 0) {
            gameService.getAllGames().remove(0);
        }
    }

    @Test
    public void getHistory_returnsCorrectChatPlayers(){

        //given
        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage1", "player");
        ChatLog testChatLog2 = new ChatLog("11:11:11", "testUser2", "2",
                "testMessage2", "player");
        chat.add(testChatLog1);
        chat.add(testChatLog2);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");
        testGame.setGameId(1L);

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());
        createdGame.addMessage("players", testChatLog1);
        createdGame.addMessage("players", testChatLog2);


        //when
        List<ChatLog> testHistory =  chatService.getHistory("players", 1L);

        //then
        assertEquals(chat.size(), testHistory.size());
        assertEquals(chat.indexOf(testChatLog1), testHistory.indexOf(testChatLog1));

    }

    @Test
    public void getHistory_returnsCorrectChatSpectators(){

        //given
        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage1", "spectators");
        ChatLog testChatLog2 = new ChatLog("11:11:11", "testUser2", "2",
                "testMessage2", "spectators");
        chat.add(testChatLog1);
        chat.add(testChatLog2);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");


        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());
        createdGame.addMessage("spectators", testChatLog1);
        createdGame.addMessage("spectators", testChatLog2);
        List<ChatLog> testHistory =  chatService.getHistory("spectators", 1L);

        //then
        assertEquals(chat.size(), testHistory.size());
        assertEquals(chat.indexOf(testChatLog1), testHistory.indexOf(testChatLog1));

    }

    @Test
    public void givenMessage_savesInCorrectChatPlayers(){

        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage1", "player");
        chat.add(testChatLog1);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");


        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());
//        createdGame.addMessage("players", testChatLog1);
//        createdGame.addMessage("players", testChatLog2);

        chatService.newMessage("players", 1L, testChatLog1);

        assertEquals(createdGame.getMessages("players"), chat);

    }

    @Test
    public void givenMessage_savesInCorrectChatSpectators(){

        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage1", "spectators");
        chat.add(testChatLog1);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());
//        createdGame.addMessage("players", testChatLog1);
//        createdGame.addMessage("players", testChatLog2);

        chatService.newMessage("spectators", 1L, testChatLog1);

        assertEquals(createdGame.getMessages("spectators"), chat);

    }

    @Test
    @Disabled
    public void givenChatPutDTO_returnCorrectChatLogPlayers(){

        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage", "players");
        chat.add(testChatLog1);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(1L);
        testGame.setPotType("Fixed");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        userService.createUser(testUser);

        ChatPutDTO chatPutDTO = new ChatPutDTO();
        chatPutDTO.setUserId(testUser.getId());
        chatPutDTO.setMessage("testMessage");

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());

        ChatLog returnedChatLog = chatService.chatPutDTOtoChatLog(chatPutDTO, "player", "players");

        assertEquals(testUser.getUsername(), returnedChatLog.getUsername());
        assertEquals(String.valueOf(testUser.getId()), returnedChatLog.getUserId());
        assertEquals(testChatLog1.getMessage(), returnedChatLog.getMessage());

    }

    @Test
    public void givenChatPutDTO_returnCorrectChatLogSpectators(){

        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage", "spectators");
        chat.add(testChatLog1);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");

        ChatPutDTO chatPutDTO = new ChatPutDTO();
        chatPutDTO.setUserId(1L);
        chatPutDTO.setMessage("testMessage");

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());

        ChatLog returnedChatLog = chatService.chatPutDTOtoChatLog(chatPutDTO, "spectator", "spectators");

        assertEquals("Spectator" + 1L, returnedChatLog.getUsername());
        assertEquals("1" , returnedChatLog.getUserId());
        assertEquals(testChatLog1.getMessage(), returnedChatLog.getMessage());

    }

    @Test
    public void givenChatLogs_returnLatestMessage(){
        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage", "players");
        ChatLog testChatLog2 = new ChatLog("22:22:22", "testUser2", "2",
                "testMessage", "players");
        chat.add(testChatLog1);
        chat.add(testChatLog2);

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());
        chatService.newMessage("players", createdGame.getGameId(), testChatLog1);
        chatService.newMessage("players", createdGame.getGameId(), testChatLog2);

        ChatLog latestChatLog = chatService.getLatestMessage(createdGame.getMessages("players"));

        assertEquals(testChatLog2.getUsername(), latestChatLog.getUsername());
        assertEquals(testChatLog2.getUserId(), latestChatLog.getUserId());
        assertEquals(testChatLog2.getMessage(), latestChatLog.getMessage());
        assertEquals(testChatLog2.getTime(), latestChatLog.getTime());

    }

    @Test
    public void givenNoChatLogs_returnEmptyLatestChatLog(){

        Game testGame = new Game();
        testGame.setGameName("TestGame");
        testGame.setGameHostID(-1L);
        testGame.setPotType("Fixed");

        Game createdGame = gameService.createGame(testGame.getGameName(), testGame.getGameHostID(), testGame.getPotType());

        ChatLog latestChatLog = chatService.getLatestMessage(createdGame.getMessages("players"));

        assertNull(latestChatLog.getUserId());
        assertNull(latestChatLog.getUsername());
        assertNull(latestChatLog.getMessage());
        assertNull(latestChatLog.getTime());

    }

}
