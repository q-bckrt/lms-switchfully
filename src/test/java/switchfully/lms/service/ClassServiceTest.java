package switchfully.lms.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;
import switchfully.lms.utility.exception.InvalidInputException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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
    @Autowired
    private UserMapper userMapper;

    User student1, student2, student3;
    User coach;

    Class classEnrolled;

    @BeforeAll
    public void beforeAll() {
        this.classRepository.deleteAll();
        this.userRepository.deleteAll();

        //CLASS CONFIGURATION ERROR -> IN USER CLASS ID IS NOT CONFIGURED CORRECTLY
        student1 = userRepository.save(new User("ann_DM","Ann Demeulemeester","ann@yahoo.com","pass", UserRole.STUDENT));
        student2 = userRepository.save(new User("jil_sander","Jil Sander","jil@yahoo.com","pass", UserRole.STUDENT));
        student3 = userRepository.save(new User("maarten_mar","Maarten Margiela","maarten@yahoo.com","pass", UserRole.STUDENT));

        coach = userRepository.save(new User("elsa_schiap","Elsa Schiaparelli","elsa@yahoo.com","pass", UserRole.COACH));
    }

    @BeforeEach
    public void beforeEach() {
        classRepository.deleteAll();

        classEnrolled = classRepository.save(new Class("JAVA"));
        classEnrolled.addCoach(coach);
        //ENROLL ALL STUDENTS!!!!!
        classEnrolled = classRepository.save(classEnrolled);
    }

    @Test
    void givenCoachAuthenticatedAndInputValid_whenCreateClass_thenReturnClassDtoListWith() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("NEW CLASS");
        //EXPECTED RESULT
        ClassOutputDtoList classOutputDto = new ClassOutputDtoList("NEW CLASS",List.of(userMapper.userToOutput(coach)));
        //WHEN THEN
        ClassOutputDtoList result = classService.createClass(classInputDto,coach);

        assertThat(result).isEqualTo(classOutputDto);
    }

    @Test
    void givenStudentAuthenticated_whenCreateClass_thenThrowsException() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("NEW CLASS");

        assertThrows(IllegalArgumentException.class, () -> classService.createClass(classInputDto,student2));
    }

    @Test
    void givenCoachAuthenticatedAndInputInValid_whenCreateClass_thenReturnClassDtoListWith() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("   ");

        assertThrows(IllegalArgumentException.class, () -> classService.createClass(classInputDto,student2));
    }

    @Test
    void givenClassWithStudentsExists_whenGetClassOverview_thenReturnClassOverview() {
//        //EXPECTED RESULT
//        ClassOutputDtoList classOutputDto = new ClassOutputDtoList("NEW CLASS",List.of(userMapper.userToOutput(coach)));
//        //WHEN THEN
//        ClassOutputDtoList result = classService.createClass(classInputDto,coach);
//
//        assertThat(result).isEqualTo(classOutputDto);
    }



}
