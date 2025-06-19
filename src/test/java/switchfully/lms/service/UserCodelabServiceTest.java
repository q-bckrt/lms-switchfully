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
import switchfully.lms.domain.Module;
import switchfully.lms.repository.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserCodelabServiceTest {

    @Autowired
    private UserCodelabRepository userCodelabRepository;
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private UserCodelabService userCodelabService;

    @BeforeEach
    public void cleanup(){
        userRepository.deleteAll();
        userRepository.flush();
        codelabRepository.deleteAll();
        codelabRepository.flush();
        userCodelabRepository.deleteAll();
        userCodelabRepository.flush();
        classRepository.deleteAll();
        classRepository.flush();
        moduleRepository.deleteAll();
        moduleRepository.flush();
        courseRepository.deleteAll();
        courseRepository.flush();
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
    }

    @Test
    void givenUserAndClassId_updateLinkBetweenUserAndCodelabOfTheCLass(){
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
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

        Class baseClass = new Class("TestClass");
        baseClass.setCourse(course);
        testUser.addClasses(baseClass);
        classRepository.save(baseClass);
        userRepository.save(testUser);

        //when
        userCodelabService.updateLinkBetweenUserAndCodelabWithClassId(testUser, baseClass.getId());


        List<UserCodelab> userCodelabs = userCodelabRepository.findAll();
        assertEquals(2, userCodelabs.size());

        for (UserCodelab uc : userCodelabs) {
            assertEquals(testUser.getId(), uc.getUser().getId());
            assertEquals(ProgressLevel.NOT_STARTED, uc.getProgressLevel());
        }
    }

    @Test
    void givenUserAlreadyLinkedToCodelab_thenNoDuplicateLinkIsCreated() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);
        Course course = new Course("course name");
        courseRepository.save(course);
        switchfully.lms.domain.Module module = new Module("module name");
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

        Class baseClass = new Class("TestClass");
        baseClass.setCourse(course);
        testUser.addClasses(baseClass);
        classRepository.save(baseClass);
        userRepository.save(testUser);

        // Add 1 existing UserCodelab
        userCodelabRepository.save(userCodelab);

        userCodelabService.updateLinkBetweenUserAndCodelabWithClassId(testUser, baseClass.getId());

        List<UserCodelab> userCodelabs = userCodelabRepository.findAll();
        assertEquals(2, userCodelabs.size(), "No duplicate should be created, only missing links added");
    }

    @Test
    void givenCodelabId_updateLinkBetweenCodelabAndAllUsersBelongingToTheSameCLassAsCodelab() {
        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
        User testUser2 = new User("Test2", "test2","testFirstname2","testLastName2", "test2@test.com", "testPassword", UserRole.STUDENT);
        userRepository.save(testUser);
        userRepository.save(testUser2);
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
        codelabRepository.save(codelab);
        Class baseClass = new Class("TestClass");
        baseClass.setCourse(course);
        testUser.addClasses(baseClass);
        testUser2.addClasses(baseClass);
        classRepository.save(baseClass);
        userRepository.save(testUser);
        userRepository.save(testUser2);

        //when
        userCodelabService.updateLinkBetweenUserAndCodelabWithCodelab(codelab);

        List<User> users = userRepository.findUsersByCodelabId(codelab.getId());
        assertEquals(2, users.size());
        assertEquals(testUser.getId(), users.get(0).getId());
        assertEquals(testUser2.getId(), users.get(1).getId());

    }
}
