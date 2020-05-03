package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSelect;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

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

    /*@BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }*/

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

}

