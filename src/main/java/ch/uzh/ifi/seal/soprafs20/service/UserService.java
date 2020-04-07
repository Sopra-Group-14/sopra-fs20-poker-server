package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        //User userByName = userRepository.findByName(userToBeCreated.getName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        //if (userByUsername != null && userByName != null) {
            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED/*BAD_REQUEST*/, String.format(baseErrorMessage, "username and the name", "are"));
        //}
        /*else*/if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, "username", "is"));
        }
        /*else if (userByName != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, "name", "is"));
        }*/
    }

    public static int addBalance(long userId){
        return -1;
    }

    public void updateUserMode(long userId, String mode){}

    public UserGetDTO getUserById(long userId){return null;}


    public User loginUser(String username, String password){

        log.warn("try login for User: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new SopraServiceException("ERROR: login not possible. Username or password incorrect.");
        }

        //throw error if password are different
        //TODO use encrypted passwords
        if(!user.getPassword().equals(password)){
            throw new SopraServiceException("ERROR: login not possible. Username or password incorrect.");
        }

        //only generate a new token, if the user is not already logged in
        if (user.getStatus() != UserStatus.ONLINE){
            String loginToken = UUID.randomUUID().toString();
            user.setToken(loginToken);
            user.setStatus(UserStatus.ONLINE);
            this.userRepository.save(user);
            this.userRepository.flush();
            return user;
        }
        return user;
    }
/*
    public void logoutUser(String token){
        User user = userRepository.findByToken(token);
        if (user != null){
            user.setStatus(UserStatus.OFFLINE);
            //user.setToken("");
            log.warn("try to logout for User: {}", user.getName());
            this.userRepository.save(user);
            this.userRepository.flush();
        }
        else{
            throw new SopraServiceException("logout failed");
        }
    }
*/
}
