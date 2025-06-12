package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.service.dto.*;
import switchfully.lms.utility.security.KeycloakUserDTO;

import java.util.List;

@Component
public class UserMapper {


    public User inputToUser(UserInputDto userInputDto) {
        String displayName = userInputDto.getFirstName() + " " + userInputDto.getLastName();
        return new User(
                userInputDto.getUserName(),
                displayName,
                userInputDto.getFirstName(),
                userInputDto.getLastName(),
                userInputDto.getEmail(),
                userInputDto.getPassword(),
                UserRole.STUDENT
        );
    }

    public KeycloakUserDTO userInputToKeycloakUser(UserInputDto userInputDto) {
        return new KeycloakUserDTO(
                userInputDto.getUserName(),
                userInputDto.getFirstName(),
                userInputDto.getLastName(),
                userInputDto.getPassword(),
                UserRole.STUDENT,
                userInputDto.getEmail()
        );
    }

    public KeycloakUserDTO userEditToKeycloakUser(User user, UserInputEditDto userInputEditDto) {
        return new KeycloakUserDTO(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                userInputEditDto.getPassword(),
                UserRole.STUDENT,
                user.getEmail()
        );
    }


    public UserOutputDtoList userToOutputList(User user, List<ClassOutputDto> classList) {
        return new UserOutputDtoList(
                user.getUserName(),
                user.getDisplayName(),
                user.getEmail(),
                classList

        );
    }

    public UserOutputDto userToOutput(User user) {
        return new UserOutputDto(
                user.getUserName(),
                user.getDisplayName(),
                user.getEmail()
        );
    }

}
