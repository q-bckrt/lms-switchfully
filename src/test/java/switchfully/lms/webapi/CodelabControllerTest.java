package switchfully.lms.webapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.TestSecurityConfig;
import switchfully.lms.domain.*;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Module;
import switchfully.lms.repository.*;
import switchfully.lms.service.dto.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
@Rollback
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CodelabControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserCodelabRepository userCodelabRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        userCodelabRepository.deleteAll();
        userCodelabRepository.flush();
        commentRepository.deleteAll();
        commentRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
        submoduleRepository.deleteAll();
        codelabRepository.deleteAll();
        submoduleRepository.flush();
        codelabRepository.flush();
        userCodelabRepository.deleteAll();
        userCodelabRepository.flush();
    }

    @Test
    void testCreateNewCodelab() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details about the codelab", parentSubmodule.getId());

        //when
        given()
            .contentType("application/json")
            .body(codelabInputDto)
        .when()
            .post("/codelabs")
        .then()
            .statusCode(201)
            .body("title", equalTo("Test Codelab"))
            .body("details", equalTo("Details about the codelab"))
            .body("parentSubmoduleId", equalTo(parentSubmodule.getId().intValue()));
    }

    @Test
    void testGetAllCodelabs() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto1 = new CodelabInputDto("Codelab 1", "Details for codelab 1", parentSubmodule.getId());
        CodelabInputDto codelabInputDto2 = new CodelabInputDto("Codelab 2", "Details for codelab 2", parentSubmodule.getId());

        given()
            .contentType("application/json")
            .body(codelabInputDto1)
        .when()
            .post("/codelabs");

        given()
            .contentType("application/json")
            .body(codelabInputDto2)
        .when()
            .post("/codelabs");

        //when
        given()
            .when()
            .get("/codelabs")
        .then()
            .statusCode(200)
            .body("$", hasSize(2))
            .body("title", containsInAnyOrder("Codelab 1", "Codelab 2"));
    }

    @Test
    void testGetCodelabById() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details about the codelab", parentSubmodule.getId());
        int codelabId = given()
            .contentType("application/json")
            .body(codelabInputDto)
        .when()
            .post("/codelabs")
            .then()
            .statusCode(201)
            .extract()
            .path("id");

        //when
        given()
            .when()
            .get("/codelabs/" + codelabId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Test Codelab"))
            .body("details", equalTo("Details about the codelab"))
            .body("parentSubmoduleId", equalTo(parentSubmodule.getId().intValue()));
    }

    @Test
    void testUpdateCodelab() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details about the codelab", parentSubmodule.getId());
        int codelabId = given()
            .contentType("application/json")
            .body(codelabInputDto)
        .when()
            .post("/codelabs")
            .then()
            .statusCode(201)
            .extract()
            .path("id");

        CodelabInputDto updatedCodelabInputDto = new CodelabInputDto("Updated Codelab", "Updated details about the codelab", parentSubmodule.getId());

        //when
        given()
            .contentType("application/json")
            .body(updatedCodelabInputDto)
        .when()
            .put("/codelabs/" + codelabId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Codelab"))
            .body("details", equalTo("Updated details about the codelab"))
            .body("parentSubmoduleId", equalTo(parentSubmodule.getId().intValue()));
    }

    @Test
    void givenUserCodelabExistsAndInputIsValid_whenPostComment_thenReturnCommentOutputDto() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //GIVEN
        CommentInputDto input = new CommentInputDto("valid comment");
        User user = userRepository.save(new User("userName","displayName","firstName","lastName","email@yahoo.com","password", UserRole.STUDENT));
        Submodule submodule = submoduleRepository.save(new Submodule("Parent Submodule"));
        Codelab codelab = codelabRepository.save(new Codelab("codelabtitle","codelabdetails",submodule));
        UserCodelab userCodelab = new UserCodelab(user,codelab,ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);
        //EXPECTED
        //null values in expected output dto are not being tested on
        CommentOutputDto expectedResult = new CommentOutputDto(null,user.getDisplayName(),codelab.getTitle(),input.getComment(), null);
        //RESULT
        CommentOutputDto response = given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/codelabs/"+codelab.getId()+"/comments/"+user.getUserName())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CommentOutputDto.class);

        assertThat(response.getComment()).isEqualTo(expectedResult.getComment());
        assertThat(response.getCodelabTitle()).isEqualTo(expectedResult.getCodelabTitle());
        assertThat(response.getUserDisplayName()).isEqualTo(expectedResult.getUserDisplayName());

    }
}
