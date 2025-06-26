package switchfully.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import switchfully.lms.domain.*;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.*;
import switchfully.lms.service.dto.*;
import switchfully.lms.utility.exception.InvalidInputException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private UserCodelabRepository userCodelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void cleanup(){
        userRepository.deleteAll();
        userRepository.flush();
    }


    @Test
    void givenUserName_getProfileInformation(){
        // given user
        User testUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        // when
        UserOutputDtoList retrievedUser = userService.getProfile("test");

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(retrievedUser.getDisplayName()).isEqualTo(testUser.getDisplayName());
        assertThat(retrievedUser.getClasses().isEmpty()).isTrue();
    }

    @Test
    void givenUserNameAndEditDto_ifAlreadyExistingUsername_throwException(){
        // given user and userInputEditDto
        User testUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User testUser2 = new User("test2", "test2","testFirstname","testLastName", "test2@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        userRepository.save(testUser2);
        UserInputEditDto userInputEdit = new UserInputEditDto( "New Test", "PassTest");

        assertThrows(Exception.class, ()-> userService.updateProfile(userInputEdit, testUser.getUserName()));

    }

    @Test
    void givenClassId_updateListClassesOfUser(){
        // given user and userInputEditDto
        User testUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        Class baseClass = new Class("TestClass");
        classRepository.save(baseClass);
        Course course = new Course("TestCourse");
        courseRepository.save(course);
        baseClass.setCourse(course);
        classRepository.save(baseClass);
        // when
        UserOutputDtoList userResult = userService.updateClassInfo(1L, testUser.getUserName());

        //then
        assertThat(userResult).isNotNull();
        assertThat(userResult.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(userResult.getClasses()).hasSize(1);
        assertThat(userResult.getClasses().get(0).getTitle()).isEqualTo("TestClass");
    }

    @Test
    void givenNonExistingClassId_throwException(){
        // given user and userInputEditDto
        User testUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);

        //then
        assertThrows(Exception.class, ()-> userService.updateClassInfo(4L, testUser.getUserName()));
    }

    @Test
    void givenUsername_getProgressLevelForAllCodelabOfAUser(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        codelabRepository.save(codelab);

        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.BUSY);
        userCodelabRepository.save(userCodelab);

        //when
        ProgressPerUserDtoList progressList = userService.getCodelabProgressPerUser(testUser.getUserName());

        assertThat(progressList).isNotNull();
        assertThat(progressList.getUsername()).isEqualTo(testUser.getUserName());
        assertThat(progressList.getProgressPerUserDtoList()).hasSize(1);
        assertThat(progressList.getProgressPerUserDtoList().get(0).getProgressLevel()).isEqualTo(ProgressLevel.BUSY);

    }

    @Test
    void givenUsernameCodelabIdProgressLevel_updateProgressLevel(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        codelabRepository.save(codelab);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.BUSY);
        userCodelabRepository.save(userCodelab);

        //when
        Boolean updateDone = userService.updateProgressLevel(testUser.getUserName(), codelab.getId(),"STUCK");

        assertThat(updateDone).isTrue();

        UserCodelabId userCodelabId = new UserCodelabId(testUser.getId(),codelab.getId());
        UserCodelab userCodelabUpdated = userCodelabRepository.findById(userCodelabId).orElseThrow(() -> new InvalidInputException("This user and codelab pair does not exist."));

        assertThat(userCodelabUpdated.getProgressLevel()).isEqualTo(ProgressLevel.STUCK);
    }
}
