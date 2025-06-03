package switchfully.lms.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.mapper.ClassMapper;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ClassServiceTest {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void beforeAll() {
        this.classRepository.deleteAll();
        //this.userRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() {

    }



}
