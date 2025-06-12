package switchfully.lms.webapi;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
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
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.UserRole;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.ClassService;
import switchfully.lms.service.UserService;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
@Rollback
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
    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    User student1, student2, student3;
    UserOutputDto student1Dto, student2Dto, student3Dto;
    User coach;
    UserOutputDto coachDto;

    Class classEnrolled;
    Course courseJava;

    Long lastClassId;
    Long lastCourseId;


    @BeforeEach
    @Commit
    void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        classRepository.deleteAll();
        userRepository.deleteAll();
        courseRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        student1 = userRepository.save(new User("ann_DM","Ann Demeulemeester","testFirstname","testLastName","ann@yahoo.com","pass", UserRole.STUDENT));
        student2 = userRepository.save(new User("jil_sander","Jil Sander","testFirstname","testLastName","jil@yahoo.com","pass", UserRole.STUDENT));
        student3 = userRepository.save(new User("maarten_mar","Maarten Margiela","testFirstname","testLastName","maarten@yahoo.com","pass", UserRole.STUDENT));

        student1Dto = new UserOutputDto(student1.getUserName(),student1.getDisplayName(),student1.getEmail());
        student2Dto = new UserOutputDto(student2.getUserName(),student2.getDisplayName(),student2.getEmail());
        student3Dto = new UserOutputDto(student3.getUserName(),student3.getDisplayName(),student3.getEmail());

        student1.setClasses(new ArrayList<>());
        student2.setClasses(new ArrayList<>());
        student3.setClasses(new ArrayList<>());

        coach = userRepository.save(new User("elsa_schiap","Elsa Schiaparelli","testFirstname","testLastName","elsa@yahoo.com","pass", UserRole.COACH));

        coachDto = new UserOutputDto(coach.getUserName(),coach.getDisplayName(),coach.getEmail());

        coach.setClasses(new ArrayList<>());

        classEnrolled = classRepository.save(new Class("JAVA_2025"));
        courseJava = courseRepository.save(new Course("JAVA BASICS"));

        classEnrolled.setCourse(courseJava);

        student1.addClasses(classEnrolled);
        student2.addClasses(classEnrolled);
        student3.addClasses(classEnrolled);
        coach.addClasses(classEnrolled);

        lastClassId = ((Number) entityManager.createNativeQuery("SELECT nextval('classes_seq')").getSingleResult()).longValue();
        lastCourseId = ((Number) entityManager.createNativeQuery("SELECT nextval('courses_seq')").getSingleResult()).longValue();
    }

    @Test
    void givenValidPayloadAndAuth_whenCreateClass_thenReturnClassDtoList() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

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
                .post("/classes/"+coach.getUserName())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ClassOutputDtoList.class);

        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    void givenUserNotCoach_whenCreateClass_thenThrowException() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        ClassInputDto input = new ClassInputDto("NEW CLASS");
        String token = "GENERATE TOKEN HERE";

        //CHANGE TO HttpStatus.UNAUTHORIZED WHEN AUTHORIZATION IMPLEMENTED
        given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/classes/"+student1.getUserName())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void givenValidUserIsEnrolled_whenGetClassOverView_thenReturnClassDtoList() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //EXPECTED
        CourseOutputDto courseDto = new CourseOutputDto(courseJava.getId(),courseJava.getTitle(),new ArrayList<>());
        ClassOutputDtoList expectedResult = new ClassOutputDtoList(classEnrolled.getId(),courseDto,classEnrolled.getTitle(), List.of(coachDto,
                student1Dto,student2Dto,student3Dto));
        //RESULT
        ClassOutputDtoList response = given()
                .when()
                .get("/classes/classOverview/"+coach.getUserName()+"/"+classEnrolled.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ClassOutputDtoList.class);

        //THIS LETS ME IGNORE THE ORDER AND ONLY FOCUS ON CONTENT WHEN COMPARING ENTIRE OBJECTS CONTAINING LISTS!!!!!!!
        assertThat(response).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedResult);
    }

    @Test
    void givenValidUserIsNotEnrolled_whenGetClassOverView_thenThrowException() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        User newStudent = userRepository.save(new User("newname","newname","testFirstname","testLastName","new@yahoo.com","pass", UserRole.STUDENT));
        //RESULT
        given()
                .when()
                .get("/classes/classOverview/"+coach.getUserName()+"/"+newStudent.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void givenValidAuthorizedCoachAndClassAndCourseExists_whenLinkCourseToClass_thenReturnClassDto() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Class newClass = classRepository.save(new Class("NEW CLASS"));
        Course newCourse = courseRepository.save(new Course("NEW COURSE"));



        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(newClass.getId(),newClass.getTitle());
        //RESULT
        ClassOutputDto response = given()
                .when()
                .put("/classes/linkCourseClass/"+newClass.getId()+"/"+newCourse.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ClassOutputDto.class);

        assertThat(response).isEqualTo(expectedResult);
        assertThat(classRepository.findById(newClass.getId()).get().getCourse().getTitle()).isEqualTo(newCourse.getTitle());
    }

    @Test
    void givenValidAuthorizedCoachAndClassAndCourseExistsAndClassAlreadyHasCourse_whenLinkCourseToClass_thenReturnClassDtoWithChangedCourse() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Course newCourse = courseRepository.save(new Course("NEW COURSE"));

        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle());
        //RESULT
        ClassOutputDto response = given()
                .when()
                .put("/classes/linkCourseClass/"+classEnrolled.getId()+"/"+newCourse.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ClassOutputDto.class);

        assertThat(response).isEqualTo(expectedResult);
        assertThat(classRepository.findById(classEnrolled.getId()).get().getCourse().getTitle()).isEqualTo(newCourse.getTitle());
    }

    @Test
    void givenMultipleClassesExistAndAuthorizedCoach_whenFindAllClasses_thenReturnClassDtos() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Class newClass1 = classRepository.save(new Class("NEW CLASS1"));
        Class newClass2 = classRepository.save(new Class("NEW CLASS2"));

        //EXPECTED
        List<ClassOutputDto> expectedResult = List.of(new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle()),
                new ClassOutputDto(newClass1.getId(),newClass1.getTitle()),
                new ClassOutputDto(newClass2.getId(),newClass2.getTitle()));
        //RESULT
        List<ClassOutputDto> response = given()
                .when()
                .get("/classes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<ClassOutputDto>>() {});

        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    void givenClassExistsInRepoAndAuthorizedCoach_whenFindClassById_thenReturnClassDtos() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Class newClass1 = classRepository.save(new Class("NEW CLASS1"));
        Class newClass2 = classRepository.save(new Class("NEW CLASS2"));

        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle());

        ClassOutputDto response = given()
                .when()
                .get("/classes/"+classEnrolled.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ClassOutputDto.class);

        assertThat(response).isEqualTo(expectedResult);
    }

}
