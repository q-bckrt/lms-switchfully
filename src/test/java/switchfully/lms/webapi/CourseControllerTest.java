package switchfully.lms.webapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import switchfully.lms.TestSecurityConfig;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.CourseInputDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CourseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        courseRepository.deleteAll();
        moduleRepository.deleteAll();
        courseRepository.flush();
        moduleRepository.flush();
    }

    @Test
    void testCreateNewCourse() {
        //given
        CourseInputDto courseInputDto = new CourseInputDto("test");

        given()
                .contentType("application/json")
                .body(courseInputDto)
                .when()
                .post("/courses")
                .then()
                .statusCode(201)
                .body("title", equalTo(courseInputDto.getTitle()));
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course("course1");
        Course course2 = new Course("course2");
        courseRepository.save(course1);
        courseRepository.save(course2);

        given()
                .when()
                .get("/courses")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].title", equalTo(course1.getTitle()))
                .body("[1].title", equalTo(course2.getTitle()));
    }

    @Test
    void testGetCourseById() {
        Course course1 = new Course("course1");
        courseRepository.save(course1);
        given()
                .when()
                .get("/courses/" + course1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(course1.getTitle()));
    }

    @Test
    void testUpdateCourseTitle() {
        Course course1 = new Course("course1");
        courseRepository.save(course1);
        CourseInputDto courseInputDto = new CourseInputDto("changedTest");

        given()
                .contentType("application/json")
                .body(courseInputDto)
                .when()
                .put("/courses/" + course1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(courseInputDto.getTitle()));

    }

    @Test
    void testAddModuleToCourse() {
        Course course1 = new Course("course1");
        courseRepository.save(course1);
        Module module1 = new Module("module1");
        moduleRepository.save(module1);

        given()
                .when()
                .put("courses/"+course1.getId() + "/modules/" + module1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(course1.getTitle()))
                .body("childModules.size()", equalTo(1))
                .body("childModules[0]", equalTo(module1.getId().intValue()));
    }
}
