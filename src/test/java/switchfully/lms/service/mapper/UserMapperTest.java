package switchfully.lms.service.mapper;

import org.junit.jupiter.api.Test;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.service.dto.UserInputDto;
import switchfully.lms.service.dto.UserOutputDto;

import static org.assertj.core.api.Assertions.assertThat;


public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void givenValidUserInputDto_whenInputToUser_thenReturnUser() {
        //Given
        UserInputDto userInput = new UserInputDto("test","testFirstname","testLastName", "test@test.com", "testPassword");
        // Expected
        User expectedUser = new User("test", "testFirstname testLastName","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        //when
        User userResult = userMapper.inputToUser(userInput);
        //then
        assertThat(userResult).isEqualTo(expectedUser);
        assertThat(userResult.getUserName()).isEqualTo(expectedUser.getUserName());
        assertThat(userResult.getDisplayName()).isEqualTo(expectedUser.getDisplayName());
        assertThat(userResult.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    void givenUserEntity_whenUsertoOutputDto_thenReturnUserOutputDto() {
        //Given
        User user = new User("test", "testDisplay","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        // expected
        UserOutputDto expectedDto = new UserOutputDto("test","testDisplay", "test@test.com", UserRole.STUDENT);
        //when
        UserOutputDto resultDto = userMapper.userToOutput(user);
        // then
        assertThat(resultDto).isEqualTo(expectedDto);
        assertThat(resultDto.getUserName()).isEqualTo(expectedDto.getUserName());
        assertThat(resultDto.getDisplayName()).isEqualTo(expectedDto.getDisplayName());
    }

}
