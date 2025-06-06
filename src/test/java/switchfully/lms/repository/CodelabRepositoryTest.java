package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Submodule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class CodelabRepositoryTest {

    @Autowired
    private CodelabRepository codelabRepository;

    @BeforeEach
    public void setUp() {
        codelabRepository.deleteAll();
        codelabRepository.flush();
    }

    @Test
    public void givenCorrectCodelab_whenSaveCodelab_theCodelabSaved() {
        Submodule submodule = new Submodule();
        Codelab codelab = new Codelab("Test Codelab", "This is a test codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);

        assertThat(savedCodelab.getId()).isNotNull();
        assertThat(savedCodelab.getTitle()).isEqualTo(codelab.getTitle());
        assertThat(savedCodelab.getDetails()).isEqualTo(codelab.getDetails());
        assertThat(savedCodelab.getParentSubmodule()).isEqualTo(codelab.getParentSubmodule());
    }

    @Test
    public void givenNullAtNonNullable_whenSave_theThrowsException() {
        Codelab codelab = new Codelab();
        assertThrows(Exception.class, () -> codelabRepository.save(codelab));
    }
}
