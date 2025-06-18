package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Submodule;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.Module;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class CodelabRepositoryTest {
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        codelabRepository.deleteAll();
        codelabRepository.flush();
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
        classRepository.deleteAll();
        classRepository.flush();
        moduleRepository.deleteAll();
        moduleRepository.flush();
        courseRepository.deleteAll();
        courseRepository.flush();
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

    @Test
    void givenClassId_shouldReturnListOfCodelabs() {
        Course course = new Course("course name");
        courseRepository.save(course);
        Class classDomain = new Class("class name");
        classDomain.setCourse(course);
        classRepository.save(classDomain);
        Module module = new Module("module name");
        course.addChildModule(module);
        moduleRepository.save(module);
        Submodule submodule = new Submodule("submodule name");
        submoduleRepository.save(submodule);
        module.addChildSubmodule(submodule);
        moduleRepository.save(module);
        Codelab codelab = new Codelab("some codelab", "details about the codelab", submodule);
        Codelab savedCodelab = codelabRepository.save(codelab);

        List<Codelab> codelabList = codelabRepository.findCodelabsByClassId(classDomain.getId());
        assertThat(codelabList).isNotNull();
        assertThat(codelabList.size()).isEqualTo(1);
        assertThat(codelabList.get(0).getTitle()).isEqualTo("some codelab");
    }

}
