package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LoginPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
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
        try {
            User createdUser = userService.createUser(userInput);

            // convert internal representation of user back to API
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
        }
        catch (Exception ex){
                throw new SopraServiceException("error");
        }

    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody LoginPutDTO loginPutDTO){
        UserGetDTO userGetDTO = new UserGetDTO();
        User user = this.userService.loginUser(loginPutDTO.getUsername(), loginPutDTO.getPassword());
        userGetDTO.setUsername(user.getUsername());
        userGetDTO.setToken(user.getToken());
        userGetDTO.setId(user.getId());
        userGetDTO.setPassword(user.getPassword());
        userGetDTO.setStatus(user.getStatus());
        userGetDTO.setName(user.getName());
        return userGetDTO;
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public void logoutUser(@RequestHeader(value = "Authorization") String token) {
        userService.logoutUser(token);
    }


    @PutMapping("/users/{userId}/balance")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long addBalance(@PathVariable long userId, @RequestBody UserPutDTO userPutDTO, @RequestHeader (value = "Authorization") String token) throws Exception {

        UserPutDTO userInput = DTOMapper.INSTANCE.convertPutDTOtoEntity(userPutDTO);

        //long userId = userInput.getUserId();
        long amount = userInput.getAmount();
        return userService.addBalance(userId, amount);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByID(@PathVariable(value= "userId") final long userId, @RequestHeader(value = "Authorization") String token){
        User user = userService.getUserById(userId);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping("/users/{userId}/mode")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserMode(@PathVariable long userId, @RequestBody String mode, @RequestHeader(value = "Authorization") String token){userService.updateUserMode(userId, mode);}

}
