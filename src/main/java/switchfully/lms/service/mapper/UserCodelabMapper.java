package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.service.dto.ProgressPerCodelabDto;
import switchfully.lms.service.dto.ProgressPerCodelabDtoList;
import switchfully.lms.service.dto.ProgressPerUserDto;
import switchfully.lms.service.dto.ProgressPerUserDtoList;

import java.util.List;

/**
 * Mapper class with methods to convert dtos to entities of the database and vice versa. <p>
 */
@Component
public class UserCodelabMapper {

    // used in the UserService
    /** Convert userCodelab to ProgressPerUser by extracting the title of the codelab and the progress Level.
     * @param userCodelab UserInputDto object
     * @return new ProgressPerUserDto object
     * */
    public ProgressPerUserDto userCodelabToProgressPerUserDto(UserCodelab userCodelab) {
        return new ProgressPerUserDto(
                userCodelab.getCodelab().getTitle(),
                userCodelab.getProgressLevel(),
                userCodelab.getCodelab().getId()
        );
    }
    /** Convert username and list of ProgressPerUser to a ProgressPerUserDtoList.
     * @param username String username of the user that wants to see his progress
     * @param progressPerUserDtoList List of progressPerUserDto
     * @return new ProgressPerUserDtoList object
     * */
    public ProgressPerUserDtoList usernameAndProgressPerUserDtoToProgressPerUserDtoList(String username, List<ProgressPerUserDto> progressPerUserDtoList) {
        return new ProgressPerUserDtoList(
                username,
                progressPerUserDtoList
        );
    }

    // used in the CodelabService
    /** Convert userCodelab to ProgressPerCodelab by extracting the display name of the user and the progress Level.
     * @param userCodelab UserInputDto object
     * @return new ProgressPerCodelabDto object
     * */
    public ProgressPerCodelabDto userCodelabToProgressPerCodelabDto(UserCodelab userCodelab) {
        return new ProgressPerCodelabDto(
                userCodelab.getUser().getDisplayName(),
                userCodelab.getProgressLevel()
        );
    }
    /** Convert username and list of ProgressPerUser to a ProgressPerUserDtoList.
     * @param codelabTitle String codelab title of the codelab for which we want an overview of the user progress
     * @param progressPerCodelabDtoList List of progressPerUserDto
     * @return new ProgressPerCodelabDtoList object
     * */
    public ProgressPerCodelabDtoList codelabTitleAndProgressPerCodelabDtoToProgressPerCodelabDtoList(String codelabTitle, List<ProgressPerCodelabDto> progressPerCodelabDtoList) {
        return new ProgressPerCodelabDtoList(
                codelabTitle,
                progressPerCodelabDtoList
        );
    }
}
