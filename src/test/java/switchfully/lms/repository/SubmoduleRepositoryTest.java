package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Submodule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class SubmoduleRepositoryTest {
    @Autowired
    private SubmoduleRepository submoduleRepository;

    @BeforeEach
    public void setUp() {
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
    }

    @Test
    void givenCorrectSubmodule_whenSaveSubmodule_theSubmoduleSaved(){
        Submodule submodule = new Submodule("submodule");
        Submodule savedSubmodule = submoduleRepository.save(submodule);

        assertThat(savedSubmodule).isNotNull();
        assertThat(savedSubmodule.getTitle()).isEqualTo("submodule");
    }

    @Test
    void givenNullAtNonNullable_whenSave_theThrowsException(){
        Submodule submodule = new Submodule();
        assertThrows(Exception.class, () -> submoduleRepository.save(submodule));
    }
}
