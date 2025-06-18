package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class UserCodelabRepositoryTest {

    @Autowired
    private UserCodelabRepository userCodelabRepository;
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
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userCodelabRepository.deleteAll();
        userCodelabRepository.flush();
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
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    void givenCorrectUserIdAndCodelabId_whenSave_thenReturnCorrectUserCodelab() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User savedUser = userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);
        UserCodelab userCodelab = new UserCodelab(savedUser,savedCodelab, ProgressLevel.NOT_STARTED);
        UserCodelab savedUserCodelab = userCodelabRepository.save(userCodelab);

        assertThat(savedUserCodelab).isNotNull();
        assertThat(savedUserCodelab.getUser().getUserName()).isEqualTo(testUser.getUserName());
        assertThat(savedUserCodelab.getCodelab().getTitle()).isEqualTo(savedCodelab.getTitle());
    }

    @Test
    void givenCorrectUserId_whenFindByUserId_thenReturnCorrectUserCodelab() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User savedUser = userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);
        UserCodelab userCodelab = new UserCodelab(savedUser,savedCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);

        List<UserCodelab> userCodelabList = userCodelabRepository.findByIdUserId(savedUser.getId());

        assertThat(userCodelabList).isNotNull();
        assertThat(userCodelabList.size()).isEqualTo(1);
        assertThat(userCodelabList.get(0).getUser().getUserName()).isEqualTo(savedUser.getUserName());
    }

    @Test
    void giveCorrectUserIdAndCodelabId_whenExistsByUserIdAndCodelabId_thenReturnTrue() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User savedUser = userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);
        UserCodelab userCodelab = new UserCodelab(savedUser,savedCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);

        Boolean foundUserCodelab = userCodelabRepository.existsByUserIdAndCodelabId(savedUser.getId(), codelab.getId());
        assertThat(foundUserCodelab).isTrue();
    }

}
