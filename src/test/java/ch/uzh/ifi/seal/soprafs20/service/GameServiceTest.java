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
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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
    @Disabled
    public void addJoiningPlayerAddsCorrectPlayer() {

        //given
        Game testGame = gameService.createGame("TestGame", -3L, "Fixed");
        //Long testGameId = testGame.getGameId();

        User testUser = new User();
        testUser.setName("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setId(-2L);
        User createdTestUser = userService.createUser(testUser);
        createdTestUser.setName("TestUser");
        Player testPlayer = new Player(createdTestUser);

        testGame.addPlayer(testPlayer);

        // when
        Player returnedPlayer = testGame.getPlayers().get(0);

        // then
        assertEquals(testPlayer.getPlayerName(), returnedPlayer.getPlayerName());

    }

}

