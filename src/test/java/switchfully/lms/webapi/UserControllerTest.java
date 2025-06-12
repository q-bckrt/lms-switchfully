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
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.utility.security.KeycloakService;

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
    @Autowired
    private KeycloakService keycloakService;

    private User user;
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
        classRepository.save(classDomain);
    }

    //BELOW IS PERSISTING TO KEYCLOAK DB --> WE WONT TEST THIS

//    @Test
//    void testCreateNewStudent(){
//        // given
//        UserInputDto userInput = new UserInputDto("test","testFirstname","testLastName", "test@test.com", "testPassword");
//
//        given()
//                .contentType("application/json")
//                .body(userInput)
//                .when()
//                .post("/users")
//                .then()
//                .statusCode(201)
//                .body("userName", equalTo("test"))
//                .body("displayName", equalTo("test"));
//    }

    @Test
    void testGetSpecificUser(){
        // given
        User expectedUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
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


    //BELOW IS PERSISTING TO KEYCLOAK DB --> WE WONT TEST THIS

//    @Test
//    void testUpdateProfileInformation(){
//        // given
//        User expectedUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
//        String username = "test";
//        userRepository.save(expectedUser);
//        UserInputEditDto userEditDto = new UserInputEditDto( "Display test", "testPassword");
//
//        given()
//                .contentType("application/json")
//                .body(userEditDto)
//                .when()
//                .put("/users/" + username + "/edit")
//                .then()
//                .statusCode(200)
//                .body("displayName", equalTo("Display test"))
//                .body("userName", equalTo("test2"));
//
//    }

    @Test
    void testUpdateClassInformation(){
        // given
        User expectedUser = new User("test", "test","testFirstname","testLastName", "test@test.com", "testPassword", UserRole.STUDENT);
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
