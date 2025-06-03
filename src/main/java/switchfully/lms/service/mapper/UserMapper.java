package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.service.dto.ClassOutputDto;
import switchfully.lms.service.dto.UserInputDto;
import switchfully.lms.service.dto.UserOutputDto;
import switchfully.lms.service.dto.UserOutputDtoList;

import java.util.List;
import java.util.stream.Collectors;

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


    public UserOutputDtoList userToOutputList(User user, List<ClassOutputDto> classList) {
        return new UserOutputDtoList(
                user.getUserName(),
                user.getDisplayName(),
                classList

        );
    }

    public UserOutputDto userToOutput(User user) {
        return new UserOutputDto(
                user.getUserName(),
                user.getDisplayName()
        );
    }

}
