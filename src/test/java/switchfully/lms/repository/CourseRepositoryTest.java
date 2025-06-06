package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.Course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        courseRepository.flush();
    }

    @Test
    void givenCorrectCourse_whenSaveCourse_thenCourseSaved() {
        Course course = new Course("Test Course");
        Course savedCourse = courseRepository.save(course);

        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    void givenNullAtNonNullable_whenSave_theThrowsException(){
       Course course = new Course();
       assertThrows(Exception.class, () -> courseRepository.save(course));
    }
}
