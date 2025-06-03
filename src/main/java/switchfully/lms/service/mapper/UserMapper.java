package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.service.dto.UserInputDto;
import switchfully.lms.service.dto.UserOutputDtoList;

@Component
public class UserMapper {

    public User inputToUser(UserInputDto userInputDto) {
        return new User(
                userInputDto.getUserName(),
                userInputDto.getUserName(),
                userInputDto.getEmail(),
                userInputDto.getPassword(),
                UserRole.STUDENT
        );
    }


    public UserOutputDtoList userToOutput(User user) {
        return new UserOutputDtoList(
                user.getUserName(),
                user.getDisplayName(),
                user.getClasses()
        );
    }

}
