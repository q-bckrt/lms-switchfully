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
    void givenCorrectCodelabWithoutParent_whenSaveCodelab_theCodelabSaved() {
        Codelab codelab = new Codelab("some codelab", "details about the codelab", null);
        Codelab savedCodelab = codelabRepository.save(codelab);

        assertThat(savedCodelab).isNotNull();
        assertThat(savedCodelab.getTitle()).isEqualTo("some codelab");
        assertThat(savedCodelab.getDetails()).isEqualTo("details about the codelab");
        assertThat(savedCodelab.getParentSubmodule()).isNull();
    }

    @Test
    void givenCorrectCodelabWithParent_whenSaveCodelab_theCodelabSaved() {
        Submodule parentSubmodule = new Submodule("parent submodule");
        Codelab codelab = new Codelab("some codelab", "details about the codelab", parentSubmodule);
        Codelab savedCodelab = codelabRepository.save(codelab);

        assertThat(savedCodelab).isNotNull();
        assertThat(savedCodelab.getTitle()).isEqualTo("some codelab");
        assertThat(savedCodelab.getDetails()).isEqualTo("details about the codelab");
        assertThat(savedCodelab.getParentSubmodule()).isEqualTo(parentSubmodule);
    }

    @Test
    void givenNullAtNonNullable_whenSave_theThrowsException() {
        Codelab codelab = new Codelab();
        assertThrows(Exception.class, () -> codelabRepository.save(codelab));
    }
}
