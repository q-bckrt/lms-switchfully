package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.service.dto.*;
import switchfully.lms.utility.security.KeycloakUserDTO;

import java.util.List;

/**
 * Mapper class with methods to convert dtos to entities of the database and vice versa. <p>
 */
@Component
public class UserMapper {

    /** Convert UserInputDto to User, the input dto contains a username, last and first name, an email and a password.
     * The display name is created here, and is composed of the first and last name concatenated together with a space separating them.
     * The role is also hardcoded here to be STUDENT, as for now, only student can register using the application.
     * @param userInputDto UserInputDto object
     * @return new User object with display name set and role hardcoded as STUDENT
     * */
    public User inputToUser(UserInputDto userInputDto) {
        String displayName = userInputDto.getFirstName() + " " + userInputDto.getLastName();
        return new User(
                userInputDto.getUserName().toLowerCase(),
                displayName,
                userInputDto.getFirstName(),
                userInputDto.getLastName(),
                userInputDto.getEmail(),
                userInputDto.getPassword(),
                UserRole.STUDENT
        );
    }

    /** Convert UserInputDto to KeycloakUserDto, the input dto contains a username, last and first name, an email and a password.
     * The role is hardcoded here to be STUDENT, as for now, only student can register using the application.
     * @param userInputDto UserInputDto object
     * @return new KeycloakUserDTO object with role hardcoded as STUDENT
     * */
    public KeycloakUserDTO userInputToKeycloakUser(UserInputDto userInputDto) {
        return new KeycloakUserDTO(
                userInputDto.getUserName().toLowerCase(),
                userInputDto.getFirstName(),
                userInputDto.getLastName(),
                userInputDto.getPassword(),
                UserRole.STUDENT,
                userInputDto.getEmail()
        );
    }

    /** Create KeycloakUserDTO from a User and UserInputEditDto in order to update the user password.
     * Only to password can be updated at the moment, enhance why we use an object User to retrieve all the already existing information about this user.
     * The role is hardcoded here to be STUDENT, as for now, only student can register using the application.
     * @param user User object
     * @param userInputEditDto UserInputDto object
     * @return new KeycloakUserDTO object with role hardcoded as STUDENT
     * */
    public KeycloakUserDTO userEditToKeycloakUser(User user, UserInputEditDto userInputEditDto) {
        return new KeycloakUserDTO(
                user.getUserName().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                userInputEditDto.getPassword(),
                UserRole.STUDENT,
                user.getEmail()
        );
    }

    /** Convert User to UserOutputDtoList.
     * This dto returns the username, display name email and list of Class object associated with a user and
     * is used in the user profile and also when updating the profile/list of classes associated with the user.
     * @param user User object
     * @param classList List of ClassOutputDto
     * @return new UserOutputDtoList object
     * */
    public UserOutputDtoList userToOutputList(User user, List<ClassOutputDto> classList) {
        return new UserOutputDtoList(
                user.getUserName().toLowerCase(),
                user.getDisplayName(),
                user.getEmail(),
                classList

        );
    }

    /** Convert User to UserOutputDto.
     * This dto returns the username, display name email and is used when creating a new student.
     * @param user User object
     * @return new UserOutputDto object
     * */
    public UserOutputDto userToOutput(User user) {
        return new UserOutputDto(
                user.getUserName().toLowerCase(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
