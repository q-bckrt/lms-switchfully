package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Class;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    private Class testClass;

    @BeforeEach
    void setUp() {testClass = classRepository.save(new Class("Test Class")) ;

    }

    @Test
    void givenClassExistInRepo_whenExistsByTitle_thenReturnTrue() {
        assertThat(classRepository.existsByTitle(testClass.getTitle())).isTrue();
    }

    @Test
    void givenClassTitleNotInRepo_whenExistsByTitle_thenReturnTrue() {
        assertThat(classRepository.existsByTitle("wrong")).isFalse();
    }
}
