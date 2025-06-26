package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.*;
import switchfully.lms.domain.Module;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
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
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
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
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
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

    @Test
    void givenSpecificUserRole_returnOnlyCodelabOfUserWithThisRole() {
        User studentUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        User coachUser = new User("coachUser", "coachUser","coachUser","coachUser", "coachUser@test.com", UserRole.COACH);
        userRepository.save(studentUser);
        userRepository.save(coachUser);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);
        UserCodelab userCodelabStudent = new UserCodelab(studentUser,savedCodelab, ProgressLevel.NOT_STARTED);
        UserCodelab userCodelabCoach = new UserCodelab(coachUser,savedCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelabStudent);
        userCodelabRepository.save(userCodelabCoach);

        List<UserCodelab> userCodelabList = userCodelabRepository.findByUserRoleCodelabId(UserRole.STUDENT, savedCodelab.getId());

        assertThat(userCodelabList).isNotNull();
        assertThat(userCodelabList.size()).isEqualTo(1);
        assertThat(userCodelabList.get(0).getUser().getUserName()).isEqualTo(studentUser.getUserName());
    }

    @Test
    void givenCourseIdAndUserId_returnListOfUserCodelabLinkToCourseAndUser(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        userRepository.save(testUser);
        Course course = new Course("course name");
        courseRepository.save(course);
        Module module = new Module("module name");
        course.addChildModule(module);
        moduleRepository.save(module);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        module.addChildSubmodule(submodule);
        moduleRepository.save(module);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab secondCodelab = new Codelab("some secondCodelab", "details about the secondCodelab", submodule);
        codelabRepository.save(codelab);
        codelabRepository.save(secondCodelab);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
        UserCodelab userCodelab1 = new UserCodelab(testUser,secondCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);
        userCodelabRepository.save(userCodelab1);

        List<UserCodelab> userCodelabFound = userCodelabRepository.findProgressByCourseIdAndUserID(course.getId(),testUser.getId());

        assertThat(userCodelabFound).isNotNull();
        assertThat(userCodelabFound.size()).isEqualTo(2);
        assertThat(userCodelabFound.get(0).getUser().getUserName()).isEqualTo(testUser.getUserName());
        assertThat(userCodelabFound.get(1).getCodelab().getTitle()).isEqualTo(userCodelab1.getCodelab().getTitle());
    }

    @Test
    void givenModuleIdAndUserId_returnListOfUserCodelabLinkToUserAndModule(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        userRepository.save(testUser);
        Module module = new Module("module name");
        moduleRepository.save(module);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        module.addChildSubmodule(submodule);
        moduleRepository.save(module);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab secondCodelab = new Codelab("some secondCodelab", "details about the secondCodelab", submodule);
        codelabRepository.save(codelab);
        codelabRepository.save(secondCodelab);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
        UserCodelab userCodelab1 = new UserCodelab(testUser,secondCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);
        userCodelabRepository.save(userCodelab1);

        List<UserCodelab> userCodelabFound = userCodelabRepository.findProgressByModuleIdAndUserID(module.getId(),testUser.getId());

        assertThat(userCodelabFound).isNotNull();
        assertThat(userCodelabFound.size()).isEqualTo(2);
        assertThat(userCodelabFound.get(0).getUser().getUserName()).isEqualTo(testUser.getUserName());
        assertThat(userCodelabFound.get(1).getCodelab().getTitle()).isEqualTo(userCodelab1.getCodelab().getTitle());
    }

    @Test
    void givenSubmoduleIdAndUserId_returnListOfUserCodelabLinkToUserAndSubmodule(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        userRepository.save(testUser);
        Submodule submodule = new Submodule("submodule name");
        Submodule submodule1 = new Submodule("submodule name1");
        submoduleRepository.save(submodule);
        submoduleRepository.save(submodule1);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab secondCodelab = new Codelab("some secondCodelab", "details about the secondCodelab", submodule1);
        codelabRepository.save(codelab);
        codelabRepository.save(secondCodelab);
        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
        UserCodelab userCodelab1 = new UserCodelab(testUser,secondCodelab, ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);
        userCodelabRepository.save(userCodelab1);

        List<UserCodelab> userCodelabList = userCodelabRepository.findProgressBySubmodulesIdAndUserID(submodule.getId(),testUser.getId());

        assertThat(userCodelabList).isNotNull();
        assertThat(userCodelabList.size()).isEqualTo(1);
        assertThat(userCodelabList.get(0).getUser().getUserName()).isEqualTo(testUser.getUserName());
        assertThat(userCodelabList.get(0).getCodelab().getTitle()).isEqualTo(userCodelab.getCodelab().getTitle());

    }


}
