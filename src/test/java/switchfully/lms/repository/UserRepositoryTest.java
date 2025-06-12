package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.flush();
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
}
