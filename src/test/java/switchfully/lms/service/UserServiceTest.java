package switchfully.lms.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.UserInputDto;
import switchfully.lms.service.dto.UserInputEditDto;
import switchfully.lms.service.dto.UserOutputDto;
import switchfully.lms.service.dto.UserOutputDtoList;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;
import switchfully.lms.domain.Class;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @BeforeEach
    public void cleanup(){
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    void givenUserInputDto_createNewUserAndSaveIt(){
        // given
        UserInputDto userInput = new UserInputDto("test", "test@test.com", "testPassword");
        // Expected
        User expectedUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        // create user
        UserOutputDto resultUser = userService.createNewStudent(userInput);

        //
        User savedUser= userRepository.findByUserName(expectedUser.getUserName());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo(expectedUser.getUserName());
        assertThat(savedUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(savedUser.getRole()).isEqualTo(expectedUser.getRole());
        assertThat(savedUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(savedUser.getDisplayName()).isEqualTo(expectedUser.getDisplayName());
    }

    @Transactional
    @Test
    void givenUserName_getProfileInformation(){
        // given user
        User testUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        // when
        UserOutputDtoList retrievedUser = userService.getProfile("test");

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(retrievedUser.getDisplayName()).isEqualTo(testUser.getDisplayName());
        assertThat(retrievedUser.getClasses().isEmpty()).isTrue();
    }

    @Transactional
    @Test
    void givenUserNameAndEditDto_updateUser(){
        // given user and userInputEditDto
        User testUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        UserInputEditDto userInputEdit = new UserInputEditDto("test", "New Test", "PassTest");
        // when
        UserOutputDtoList retrievedUser = userService.updateProfile(userInputEdit, testUser.getUserName());

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(retrievedUser.getDisplayName()).isEqualTo(userInputEdit.getDisplayName());

    }

    @Transactional
    @Test
    void givenUserNameAndEditDto_ifAlreadyExistingUsername_throwException(){
        // given user and userInputEditDto
        User testUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        User testUser2 = new User("test2", "test2", "test2@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        userRepository.save(testUser2);
        UserInputEditDto userInputEdit = new UserInputEditDto("test2", "New Test", "PassTest");

        assertThrows(Exception.class, ()-> userService.updateProfile(userInputEdit, testUser.getUserName()));

    }

    @Transactional
    @Test
    void givenClassId_updateListClassesOfUser(){
        // given user and userInputEditDto
        User testUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);
        Class baseClass = new Class("TestClass");
        classRepository.save(baseClass);
        // when
        UserOutputDtoList userResult = userService.updateClassInfo(1L, testUser.getUserName());

        //then
        assertThat(userResult).isNotNull();
        assertThat(userResult.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(userResult.getClasses()).hasSize(1);
        assertThat(userResult.getClasses().get(0).getTitle()).isEqualTo("TestClass");
    }

    @Transactional
    @Test
    void givenNonExistingClassId_throwException(){
        // given user and userInputEditDto
        User testUser = new User("test", "test", "test@test.com", "testPassword", UserRole.STUDENT);
        testUser.setClasses(new ArrayList<>());
        userRepository.save(testUser);

        //then
        assertThrows(Exception.class, ()-> userService.updateClassInfo(4L, testUser.getUserName()));
    }
}
