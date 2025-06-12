package switchfully.lms.webapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.TestSecurityConfig;
import switchfully.lms.domain.Module;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.ModuleInputDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
@Rollback
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class ModuleControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;

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
        submoduleRepository.flush();
        moduleRepository.flush();
    }

    @Test
    void testCreateNewModule() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        //given
        ModuleInputDto moduleInputDto = new ModuleInputDto("test");

        given()
                .contentType("application/json")
                .body(moduleInputDto)
                .when()
                .post("/modules")
                .then()
                .statusCode(201)
                .body("title", equalTo(moduleInputDto.getTitle()));
    }

    @Test
    void testGetAllModules() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Module module1 = new Module("modulegetall");
        Module module2 = new Module("modulegetall2");
        moduleRepository.save(module1);
        moduleRepository.save(module2);
        moduleRepository.flush();

        given()
                .when()
                .get("/modules")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].title", equalTo(module1.getTitle()))
                .body("[1].title", equalTo(module2.getTitle()));
    }

    @Test
    void testGetModuleById(){
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Module module1 = new Module("modulebyid");
        moduleRepository.save(module1);
        moduleRepository.flush();


        given()
                .when()
                .get("/modules/" + module1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(module1.getTitle()));
    }

    @Test
    void testUpdateModuleTitle() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Module module1 = new Module("module1");
        moduleRepository.save(module1);
        ModuleInputDto moduleInputDto = new ModuleInputDto("test");

        given()
                .contentType("application/json")
                .body(moduleInputDto)
                .when()
                .put("/modules/" + module1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(moduleInputDto.getTitle()));
    }


    @Test
    void testAddSubmoduleToModule() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Module module1 = new Module("module1");
        moduleRepository.save(module1);
        Submodule submodule1 = new Submodule("submodule1");
        submoduleRepository.save(submodule1);
        moduleRepository.flush();
        submoduleRepository.flush();

        given()
                .when()
                .put("/modules/" + module1.getId() + "/submodules/" + submodule1.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(module1.getTitle()))
                .body("childSubmodules.size()", equalTo(1))
                .body("childSubmodules[0]", equalTo(submodule1.getId().intValue()));
    }
}
