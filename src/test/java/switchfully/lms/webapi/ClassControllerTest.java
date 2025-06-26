package switchfully.lms.webapi;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.TestSecurityConfig;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.UserRole;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;

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
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class ClassControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private UserRepository userRepository;
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

        student1 = userRepository.save(new User("ann_DM","Ann Demeulemeester","testFirstname","testLastName","ann@yahoo.com", UserRole.STUDENT));
        student2 = userRepository.save(new User("jil_sander","Jil Sander","testFirstname","testLastName","jil@yahoo.com", UserRole.STUDENT));
        student3 = userRepository.save(new User("maarten_mar","Maarten Margiela","testFirstname","testLastName","maarten@yahoo.com", UserRole.STUDENT));

        student1Dto = new UserOutputDto(student1.getUserName(),student1.getDisplayName(),student1.getEmail(), UserRole.STUDENT);
        student2Dto = new UserOutputDto(student2.getUserName(),student2.getDisplayName(),student2.getEmail(), UserRole.STUDENT);
        student3Dto = new UserOutputDto(student3.getUserName(),student3.getDisplayName(),student3.getEmail(), UserRole.STUDENT);

        student1.setClasses(new ArrayList<>());
        student2.setClasses(new ArrayList<>());
        student3.setClasses(new ArrayList<>());

        coach = userRepository.save(new User("elsa_schiap","Elsa Schiaparelli","testFirstname","testLastName","elsa@yahoo.com", UserRole.COACH));

        coachDto = new UserOutputDto(coach.getUserName(),coach.getDisplayName(),coach.getEmail(), UserRole.COACH);

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
        ClassOutputDtoList expectedResult = new ClassOutputDtoList(lastClassId+1,null,input.getTitle(),new ArrayList<>());
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
    void givenValidAuthorizedCoachAndClassAndCourseExists_whenLinkCourseToClass_thenReturnClassDto() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Class newClass = classRepository.save(new Class("NEW CLASS"));
        Course newCourse = courseRepository.save(new Course("NEW COURSE"));



        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(newClass.getId(),newClass.getTitle(), newCourse.getId(),newCourse.getTitle());
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
        ClassOutputDto expectedResult = new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle(), newCourse.getId(),newCourse.getTitle());
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
        Course newCourse = courseRepository.save(new Course("NEW COURSE"));
        Course newCourse1 = courseRepository.save(new Course("NEW COURSE1"));
        Course newCourse2 = courseRepository.save(new Course("NEW COURSE2"));
        classEnrolled.setCourse(newCourse);
        newClass1.setCourse(newCourse1);
        newClass2.setCourse(newCourse2);
        classRepository.save(newClass1);
        classRepository.save(newClass2);
        classRepository.save(classEnrolled);

        //EXPECTED
        List<ClassOutputDto> expectedResult = List.of(new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle(), newCourse.getId(),newCourse.getTitle()),
                new ClassOutputDto(newClass1.getId(),newClass1.getTitle(), newCourse1.getId(),newCourse1.getTitle()),
                new ClassOutputDto(newClass2.getId(),newClass2.getTitle(), newCourse2.getId(),newCourse2.getTitle()));
        //RESULT
        List<ClassOutputDto> response = given()
                .when()
                .get("/classes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<ClassOutputDto>>() {});

        System.out.println(response);

        assertThat(response.get(0).getTitle()).isEqualTo(expectedResult.get(0).getTitle());
        assertThat(response.get(0).getId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(response.get(1).getTitle()).isEqualTo(expectedResult.get(1).getTitle());
        assertThat(response.get(1).getId()).isEqualTo(expectedResult.get(1).getId());
        assertThat(response.get(2).getTitle()).isEqualTo(expectedResult.get(2).getTitle());
        assertThat(response.get(2).getId()).isEqualTo(expectedResult.get(2).getId());

    }

    @Test
    void givenClassExistsInRepoAndAuthorizedCoach_whenFindClassById_thenReturnClassDtos() {
        //NOT TAKING INTO ACCOUNT AUTHORIZATION
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        Class newClass1 = classRepository.save(new Class("NEW CLASS1"));
        Class newClass2 = classRepository.save(new Class("NEW CLASS2"));
        Course newCourse = courseRepository.save(new Course("NEW COURSE"));

        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle(), newCourse.getId(),newCourse.getTitle());

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
