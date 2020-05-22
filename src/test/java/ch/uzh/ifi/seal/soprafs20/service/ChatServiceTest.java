package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.chat.PlayerChat;
import ch.uzh.ifi.seal.soprafs20.chat.SpectatorChat;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.ChatLog;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ChatPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WebAppConfiguration
@SpringBootTest
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    private User user;
    private ChatLog chatLog;
    private List<ChatLog> chat = new ArrayList<>();
    private Game testGame;

    @Test
    public void givenChat_willReturnCorrectChatLog(){

        ChatService mockChatService = Mockito.mock(ChatService.class);

        ChatLog testChatLog = new ChatLog("11:11:11", "testUser", "1",
                                            "testMessage", "player");
        chat.add(testChatLog);

        given(mockChatService.getHistory("players", 1L)).willReturn(chat);

        List<ChatLog> testHistory = mockChatService.getHistory("players", 1L);

        assertEquals(testChatLog.getMessage(), testHistory.get(0).getMessage());
        assertEquals(testChatLog.getTime(), testHistory.get(0).getTime());
        assertEquals(testChatLog.getUserId(), testHistory.get(0).getUserId());
        assertEquals(testChatLog.getUsername(), testHistory.get(0).getUsername());
    }

    @Test
    public void givenChatPutDTO_willAddToPlayerChat(){

        ChatService mockChatService = Mockito.mock(ChatService.class);
        ChatPutDTO chatPutDTO = new ChatPutDTO();
        chatPutDTO.setMessage("testMessage");
        chatPutDTO.setUserId(1L);
        ChatLog testChatLog = new ChatLog("11:11:11", "testUser", "1",
                "testMessage", "player");


        given(mockChatService.chatPutDTOtoChatLog(chatPutDTO, "player", "players")).willReturn(testChatLog);


        ChatLog returnedChatLog = mockChatService.chatPutDTOtoChatLog(chatPutDTO, "player", "players");

        assertEquals(String.valueOf(chatPutDTO.getUserId()), returnedChatLog.getUserId());
        assertEquals(chatPutDTO.getMessage(), returnedChatLog.getMessage());

    }


    @Test
    public void givenChatPutDTO_willAddToSpectatorChat(){

        ChatService mockChatService = Mockito.mock(ChatService.class);
        ChatPutDTO chatPutDTO = new ChatPutDTO();
        chatPutDTO.setMessage("testMessage");
        chatPutDTO.setUserId(1L);
        ChatLog testChatLog = new ChatLog("11:11:11", "testUser", "1",
                "testMessage", "player");


        given(mockChatService.chatPutDTOtoChatLog(chatPutDTO, "spectator", "spectators")).willReturn(testChatLog);

        ChatLog returnedChatLog = mockChatService.chatPutDTOtoChatLog(chatPutDTO, "spectator", "spectators");

        assertEquals(String.valueOf(chatPutDTO.getUserId()), returnedChatLog.getUserId());
        assertEquals(chatPutDTO.getMessage(), returnedChatLog.getMessage());

    }

    @Test
    public void givenChatHistory_returnLatestMessage(){
        ChatService mockChatService = Mockito.mock(ChatService.class);

        ChatLog testChatLog1 = new ChatLog("11:11:11", "testUser1", "1",
                "testMessage2", "player");
        ChatLog testChatLog2 = new ChatLog("22:22:22", "testUser2", "2",
                "testMessage2", "player");
        chat.add(testChatLog1);
        chat.add(testChatLog2);

        given(mockChatService.getLatestMessage(chat)).willReturn(chat.get(1));

        ChatLog latestChatLog = mockChatService.getLatestMessage(chat);

        assertEquals(latestChatLog.getUserId(), testChatLog2.getUserId());
        assertEquals(latestChatLog.getUsername(), testChatLog2.getUsername());
        assertEquals(latestChatLog.getTime(), testChatLog2.getTime());
        assertEquals(latestChatLog.getMessage(), testChatLog2.getMessage());

    }



}
