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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.TestSecurityConfig;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassRepository classRepository;


    private Class classDomain;



    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRepository.deleteAll();
        classRepository.deleteAll();
        userRepository.flush();
        classRepository.flush();
        classDomain = new Class("TestClass");
        classDomain = classRepository.save(classDomain);
        classRepository.flush();
    }

    @Test
    void testGetSpecificUser(){
        // given
        User expectedUser = new User("test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        userRepository.save(expectedUser);
        String username = "test";
        given()
                .when()
                //.auth().oauth2(tokenStudent)
                .get("/users/" + username)
                .then()
                .statusCode(200)
                .body("displayName", equalTo("test"))
                .body("userName", equalTo(username));

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void testUpdateClassInformation(){
        // given
        User expectedUser = new User("test", "test","testFirstname","testLastName", "test@test.com", UserRole.STUDENT);
        String username = "test";
        userRepository.save(expectedUser);

        given()
                .when()
                .put("/users/" + username + "/edit/class?classId=" + classDomain.getId())
                .then()
                .statusCode(200)
                .body("displayName", equalTo("test"))
                .body("userName", equalTo(username))
                .body("classes[0].title", equalTo(classDomain.getTitle()) );
    }
}
