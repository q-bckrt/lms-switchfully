package switchfully.lms.webapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Module;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.SubmoduleInputDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
@Rollback
public class SubmoduleControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private CodelabRepository codelabRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        //to remove link from join table:
        for (Module module : moduleRepository.findAll()) {
            module.getChildSubmodules().clear();
        }
        moduleRepository.flush();
        submoduleRepository.deleteAll();
        moduleRepository.deleteAll();
        codelabRepository.deleteAll();
        submoduleRepository.flush();
        moduleRepository.flush();
        codelabRepository.flush();
    }

    @Test
    void testCreateNewSubmodule() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        //given
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("test");

        given()
                .contentType("application/json")
                .body(submoduleInputDto)
                .when()
                .post("/submodules")
                .then()
                .statusCode(201)
                .body("title", equalTo(submoduleInputDto.getTitle()));
    }

    @Test
    void testGetAllSubmodules() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);
        Submodule submodule2 = new Submodule("test2");
        submoduleRepository.save(submodule2);

        given()
                .when()
                .get("/submodules")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].title", equalTo(submodule.getTitle()))
                .body("[1].title", equalTo(submodule2.getTitle()));
    }

    @Test
    void testGetSubmoduleById(){
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);

        given()
                .when()
                .get("/submodules/" + submodule.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(submodule.getTitle()));
    }

    @Test
    void testUpdateSubmoduleTitle() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("testUpdate");

        given()
                .contentType("application/json")
                .body(submoduleInputDto)
                .when()
                .put("/submodules/" + submodule.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(submoduleInputDto.getTitle()));
    }
}
