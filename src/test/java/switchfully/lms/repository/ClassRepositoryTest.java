package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Class;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    private Class testClass;

    @BeforeEach
    void setUp() {
        classRepository.deleteAll();
        testClass = classRepository.save(new Class("Test Class"));
        classRepository.flush();
    }

    @Test
    void givenClassExistInRepo_whenExistsByTitle_thenReturnTrue() {
        assertThat(classRepository.existsByTitle(testClass.getTitle())).isTrue();
    }

    @Test
    void givenClassTitleNotInRepo_whenExistsByTitle_thenReturnTrue() {
        assertThat(classRepository.existsByTitle("wrong")).isFalse();
    }

    @Test
    void givenCorrectInput_whenSave_thenReturnCorrectClass() {
        Class  newClass = new Class("new Test Class");
        Class savedClass = classRepository.save(newClass);

        assertThat(savedClass.getId()).isNotNull();
        assertThat(savedClass.getTitle()).isEqualTo(newClass.getTitle());
        assertThat(savedClass.getUsers()).isEmpty();
        assertThat(savedClass.getCourse()).isEqualTo(null);
    }

    @Test
    void givenInvalidTitle_whenSave_thenThrowException() {
        Class  newClass = new Class(null);

        assertThrows(Exception.class, () -> classRepository.save(newClass));
    }


}
