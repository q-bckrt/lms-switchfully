package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.service.dto.ProgressPerUserDto;
import switchfully.lms.service.dto.ProgressPerUserDtoList;

import java.util.List;

/**
 * Mapper class with methods to convert dtos to entities of the database and vice versa. <p>
 */
@Component
public class UserCodelabMapper {

    // used in the UserService
    public ProgressPerUserDto userCodelabToProgressPerUserDto(UserCodelab userCodelab) {
        return new ProgressPerUserDto(
                userCodelab.getCodelab().getTitle(),
                userCodelab.getProgressLevel()
        );
    }
    /** Convert username and list of ProgressPerUser to a ProgressPerUserDtoList.
     * @param username UserInputDto object
     * @param progressPerUserDtoList List of progressPerUserDto
     * @return new User object with display name set and role hardcoded as STUDENT
     * */
    public ProgressPerUserDtoList usernameAndProgressPerUserDtoToProgressPerUserDtoList(String username, List<ProgressPerUserDto> progressPerUserDtoList) {
        return new ProgressPerUserDtoList(
                username,
                progressPerUserDtoList
        );
    }
}
