package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class ModuleRepositoryTest {

    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    void setUp() {
        moduleRepository.deleteAll();
        moduleRepository.flush();
    }

    @Test
    void givenCorrectModule_whenSaveModule_theModuleSaved(){
        Module module = new switchfully.lms.domain.Module("test");
        Module savedModule = moduleRepository.save(module);

        assertThat(savedModule.getId()).isNotNull();
        assertThat(savedModule.getTitle()).isEqualTo(module.getTitle());
    }

    @Test
    void givenNullAtNonNullable_whenSave_theThrowsException(){
        Module module = new Module();
        assertThrows(Exception.class, () -> moduleRepository.save(module));
    }
}
