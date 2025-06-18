package switchfully.lms.service.mapper;

import org.junit.jupiter.api.Test;
import switchfully.lms.domain.*;
import switchfully.lms.service.dto.ProgressPerCodelabDto;
import switchfully.lms.service.dto.ProgressPerCodelabDtoList;
import switchfully.lms.service.dto.ProgressPerUserDto;
import switchfully.lms.service.dto.ProgressPerUserDtoList;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class UserCodelabMapperTest {

    private final UserCodelabMapper userCodelabMapper = new UserCodelabMapper();


    @Test
    void givenCorrectUserCodelab_whenUserCodelabToProgressPerUserDto_thenReturnProgressPerUserDto() {
        //given
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        Submodule submodule = new Submodule("submodule name");
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
        //expected
        ProgressPerUserDto expectedResult = new ProgressPerUserDto(codelab.getTitle(), ProgressLevel.NOT_STARTED);
        //when
        ProgressPerUserDto result = userCodelabMapper.userCodelabToProgressPerUserDto(userCodelab);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getCodelabTitle()).isEqualTo(expectedResult.getCodelabTitle());
        assertThat(result.getProgressLevel()).isEqualTo(expectedResult.getProgressLevel());
    }

    @Test
    void givenCorrectListOfProgressPerUserDto_usernameAndProgressPerUserDto_thenReturnProgressPerUserDtoList() {
        //given
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        Submodule submodule = new Submodule("submodule name");
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab codelab2 = new Codelab("some codelab2", "details about the codelab2", submodule);
        ProgressPerUserDto prUsDto = new ProgressPerUserDto(codelab.getTitle(), ProgressLevel.NOT_STARTED);
        ProgressPerUserDto prUsDto2 = new ProgressPerUserDto(codelab2.getTitle(), ProgressLevel.NOT_STARTED);
        List<ProgressPerUserDto> prgressList = Arrays.asList(prUsDto, prUsDto2);
        // expected
        ProgressPerUserDtoList prUsDtoList = new ProgressPerUserDtoList(testUser.getUserName(),prgressList);
        //when
        ProgressPerUserDtoList result = userCodelabMapper.usernameAndProgressPerUserDtoToProgressPerUserDtoList(testUser.getUserName(), prgressList);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(testUser.getUserName());
        assertThat(result.getProgressPerUserDtoList()).isEqualTo(prUsDtoList.getProgressPerUserDtoList());
    }

    @Test
    void givenCorrectUserCodelab_whenUserCodelabToProgressPerCodelabDto_thenReturnProgressPerCodelabDto() {
        //given
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        Submodule submodule = new Submodule("submodule name");
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
        //expected
        ProgressPerCodelabDto expectedResult = new ProgressPerCodelabDto(testUser.getDisplayName(), ProgressLevel.NOT_STARTED);
        //when
        ProgressPerCodelabDto result = userCodelabMapper.userCodelabToProgressPerCodelabDto(userCodelab);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getDisplayName()).isEqualTo(expectedResult.getDisplayName());
        assertThat(result.getProgressLevel()).isEqualTo(expectedResult.getProgressLevel());
    }

    @Test
    void givenCorrectListOfProgressPerUserDto_whenCodelabTitleAndProgressPerCodelabDtoToProgressPerCodelabDtoList_thenReturnProgressPerCodelabDtoList() {
        //given
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        Submodule submodule = new Submodule("submodule name");
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        ProgressPerCodelabDto prUsDto = new ProgressPerCodelabDto(testUser.getDisplayName(), ProgressLevel.NOT_STARTED);
        ProgressPerCodelabDto prUsDto2 = new ProgressPerCodelabDto(testUser.getDisplayName(), ProgressLevel.NOT_STARTED);
        List<ProgressPerCodelabDto> prgressList = Arrays.asList(prUsDto, prUsDto2);
        // expected
        ProgressPerCodelabDtoList prUsDtoList = new ProgressPerCodelabDtoList(codelab.getTitle(),prgressList);
        //when
        ProgressPerCodelabDtoList result = userCodelabMapper.codelabTitleAndProgressPerCodelabDtoToProgressPerCodelabDtoList(codelab.getTitle(), prgressList);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getCodelabTitle()).isEqualTo(codelab.getTitle());
        assertThat(result.getProgressPerCodelabDtoList()).isEqualTo(prUsDtoList.getProgressPerCodelabDtoList());
    }
}
