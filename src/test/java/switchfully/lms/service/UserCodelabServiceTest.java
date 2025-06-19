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

//    @Test
//    void givenUserAndClassId_updateLinkBetweenUserAndCodelabOfTheCLass(){
//        User testUser = new User("Test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
//        userRepository.save(testUser);
//        Course course = new Course("course name");
//        courseRepository.save(course);
//        switchfully.lms.domain.Module module = new Module("module name");
//        course.addChildModule(module);
//        moduleRepository.save(module);
//        Submodule submodule = new Submodule("submodule name");
//        submoduleRepository.save(submodule);
//        module.addChildSubmodule(submodule);
//        moduleRepository.save(module);
//        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
//        Codelab secondCodelab = new Codelab("some secondCodelab", "details about the secondCodelab", submodule);
//        codelabRepository.save(codelab);
//        codelabRepository.save(secondCodelab);
//        UserCodelab userCodelab = new UserCodelab(testUser,codelab, ProgressLevel.NOT_STARTED);
//        UserCodelab userCodelab1 = new UserCodelab(testUser,secondCodelab, ProgressLevel.NOT_STARTED);
//
//        // Create and save class
//        Class baseClass = new Class("TestClass");
//        classRepository.save(baseClass);
//
//        // ACT: Call the service method
//        userCodelabService.updateLinkBetweenUserAndCodelabWithClassId(testUser, baseClass.getId());
//
//        // ASSERT: UserCodelab records were created
//        List<UserCodelab> userCodelabs = userCodelabRepository.findAll();
//        assertEquals(2, userCodelabs.size());
//
//        for (UserCodelab uc : userCodelabs) {
//            assertEquals(testUser.getId(), uc.getUser().getId());
//            assertEquals(ProgressLevel.NOT_STARTED, uc.getProgressLevel());
//        }
//    }




//    @Test
//    void givenUserAlreadyLinkedToCodelab_thenNoDuplicateLinkIsCreated() {
//        // Setup same as before...
//        // Add 1 existing UserCodelab
//        userCodelabRepository.save(new UserCodelab(testUser, codelab1, ProgressLevel.NOT_STARTED));
//
//        // Act
//        userCodelabService.updateLinkBetweenUserAndCodelabWithClassId(testUser, classEntity.getId());
//
//        // Assert
//        List<UserCodelab> userCodelabs = userCodelabRepository.findAll();
//        assertEquals(2, userCodelabs.size(), "No duplicate should be created, only missing links added");
//    }
}
