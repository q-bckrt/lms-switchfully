package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.*;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.Module;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.flush();
        codelabRepository.deleteAll();
        codelabRepository.flush();
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
        classRepository.deleteAll();
        classRepository.flush();
        moduleRepository.deleteAll();
        moduleRepository.flush();
        courseRepository.deleteAll();
        courseRepository.flush();
    }

    @Test
    void givenCorrectUser_whenSave_thenReturnCorrectUser() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(savedUser.getDisplayName()).isEqualTo(testUser.getDisplayName());
        assertThat(savedUser.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getRole()).isEqualTo(testUser.getRole());

    }

    @Test
    void givenNullAtNonNullable_whenSave_theThrowsException() {
        User nullUsername = new User(null, "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User nullDisplayName = new User("test", null,"testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User nullEmail = new User("test", "test", null,"testFirstname","testLastName", "testPassword", UserRole.STUDENT);
        User nullPassword = new User("test", "test","testFirstname","testLastName", "test@test.com", null, UserRole.STUDENT);
        User nullRole = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", null);
        User nullFirstName = new User("test", "test",null,"testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User nullLastName = new User("test", "test","testFirstName",null, "test@test.com", "testPassword", UserRole.STUDENT);



        assertThrows(Exception.class, () -> userRepository.save(nullUsername));
        assertThrows(Exception.class, () -> userRepository.save(nullDisplayName));
        assertThrows(Exception.class, () -> userRepository.save(nullEmail));
        assertThrows(Exception.class, () -> userRepository.save(nullPassword));
        assertThrows(Exception.class, () -> userRepository.save(nullRole));
        assertThrows(Exception.class, () -> userRepository.save(nullFirstName));
        assertThrows(Exception.class, () -> userRepository.save(nullLastName));
    }

    @Test
    void givenCorrectUser_whenFindByEmail_thenReturnCorrectUser() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);

        User foundUser = userRepository.findByEmail(testUser.getEmail());

        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo(testUser.getUserName());
        assertThat(foundUser.getDisplayName()).isEqualTo(testUser.getDisplayName());
        assertThat(foundUser.getPassword()).isEqualTo(testUser.getPassword());

    }

    @Test
    void givenCorrectUser_whenFindByUserName_thenReturnCorrectUser() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);

        User foundUser = userRepository.findByUserName(testUser.getUserName());

        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(foundUser.getDisplayName()).isEqualTo(testUser.getDisplayName());
        assertThat(foundUser.getPassword()).isEqualTo(testUser.getPassword());

    }

    @Test
    void givenCorrectUser_whenCheckForExistingEmail_thenReturnTrue() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);

        assertThat(userRepository.existsByEmail(testUser.getEmail())).isEqualTo(true);
    }

    @Test
    void givenCorrectUser_whenCheckForExistingUserName_thenReturnTrue() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);

        assertThat(userRepository.existsByUserName(testUser.getUserName())).isEqualTo(true);
    }

    @Test
    void givenCodelabId_retrieveListOfUsersAssociatedToIt(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);
        Course course = new Course("course name");
        courseRepository.save(course);
        Class classDomain = new Class("class name");
        classDomain.setCourse(course);
        Class savedClass = classRepository.save(classDomain);
        testUser.addClasses(savedClass);
        userRepository.save(testUser);
        Module module = new Module("module name");
        course.addChildModule(module);
        moduleRepository.save(module);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        module.addChildSubmodule(submodule);
        moduleRepository.save(module);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        codelabRepository.save(codelab);

        List<User> userList = userRepository.findUsersByCodelabId(codelab.getId());
    }
}
