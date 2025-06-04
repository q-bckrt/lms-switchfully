package switchfully.lms.webapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.UserRole;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.ClassService;
import switchfully.lms.service.UserService;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ClassControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ClassService classService;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;

    User student1, student2, student3;
    User coach;

    Class classEnrolled;

    Long lastClassId;

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        classRepository.deleteAll();
        userRepository.deleteAll();

        student1 = userRepository.save(new User("ann_DM","Ann Demeulemeester","ann@yahoo.com","pass", UserRole.STUDENT));
        student2 = userRepository.save(new User("jil_sander","Jil Sander","jil@yahoo.com","pass", UserRole.STUDENT));
        student3 = userRepository.save(new User("maarten_mar","Maarten Margiela","maarten@yahoo.com","pass", UserRole.STUDENT));

        coach = userRepository.save(new User("elsa_schiap","Elsa Schiaparelli","elsa@yahoo.com","pass", UserRole.COACH));

        classEnrolled = classRepository.save(new Class("JAVA"));
        classEnrolled.addCoach(coach);
        //ENROLL ALL STUDENTS USING SERVICE!!!!!
        classEnrolled = classRepository.save(classEnrolled);

        lastClassId = ((Number) entityManager.createNativeQuery("SELECT nextval('classes_seq')").getSingleResult()).longValue();
    }

    //all end points should have authorization, so i cant really write complete tests until authorization is implemented

    @Test
    void givenValidPayloadAndAuth_whenCreateClass_thenReturnClassDtoList() {
        //GIVEN
        ClassInputDto input = new ClassInputDto("NEW CLASS");
        String token = "GENERATE TOKEN HERE";
        //EXPECTED
        ClassOutputDtoList expectedResult = new ClassOutputDtoList(lastClassId+1,null,input.getTitle(), List.of(userMapper.userToOutput(coach)));
        //RESULT
        ClassOutputDtoList response = given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/classes")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ClassOutputDtoList.class);

        assertThat(response).isEqualTo(expectedResult);
    }

}
