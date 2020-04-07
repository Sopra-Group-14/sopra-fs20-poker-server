package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers(@RequestHeader(value = "Authorization") String token){
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody String username, @RequestBody String password){
        String token = this.userService.loginUser(username, password);
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setToken(token);
        userGetDTO.setUsername(username);
        return userGetDTO;
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public void logoutUser(@RequestHeader(value = "Authorization") String token) {
        //userService.logoutUser(token);
    }

    @GetMapping("/users/{userId}/balance")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int getBalance(@PathVariable long userId, @RequestHeader (value = "Authorization") String token){return -1;}

    @PutMapping("/users/{userId}/balance")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int addBalance(@PathVariable (value = "userId") long userId, @RequestBody int amount, @RequestHeader (value = "Authorization") String token){
        return UserService.addBalance(userId);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByID(@PathVariable(value= "userId") final long userId, @RequestHeader(value = "Authorization") String token){return userService.getUserById(userId);}

    @PutMapping("/users/{userId}/mode")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserMode(@PathVariable long userId, @RequestBody String mode, @RequestHeader(value = "Authorization") String token){userService.updateUserMode(userId, mode);}

}
