package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "balance", target = "balance")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "gameHostID", target = "gameHostID")
    @Mapping(source = "potType", target = "potType")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

//    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    UserPutDTO convertPutDTOtoEntity(UserPutDTO userPutDTO);

//    @Mapping(source =  "message", target = "message")
//    @Mapping(source = "userId", target = "userId")
//    ChatPutDTO convertPutDTOtoEntity(ChatPutDTO chatPutDTO);
//
//    @Mapping(source = "gameId", target = "gameId")
//    @Mapping(source = "chatMode", target = "chatMode")
//    ChatGetDTO convertEntityToGetDTO(ChatGetDTO chatGetDTO);

}
