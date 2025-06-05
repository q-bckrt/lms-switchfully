package switchfully.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    private List<Module> childModules = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        courseRepository.deleteAll();
        courseRepository.flush();
        moduleRepository.deleteAll();
        moduleRepository.flush();
    }

    @Test
    void givenCourseInputDto_createNewCourse() {
        //Given
        CourseInputDto courseInputDto = new CourseInputDto("test");
        //Expected
        Course course = new Course("test");
        // create course
        CourseOutputDto resultCourse = courseService.createCourse(courseInputDto);
        // saved course
        Course savedCourse = courseRepository.findById(resultCourse.getId()).get();
        assertThat(course.getTitle()).isEqualTo(savedCourse.getTitle());
    }

    @Test
    void getCourseById_retrieveCourse() {
        Course course = new Course("test");
        courseRepository.save(course);
        Course savedCourse = courseRepository.findById(course.getId()).get();
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    void getCourseById_throwsException_whenCourseNotFound() {

        assertThrows(Exception.class, () -> courseRepository.findById(50L).get());
    }

    @Transactional
    @Test
    void getAllCourses_retrieveAllCourses() {
        Course course = new Course("test");
        courseRepository.save(course);
        Course course2 = new Course("test2");
        courseRepository.save(course2);

        List<CourseOutputDto> allCourses = courseService.getAllCourses();
        assertThat(allCourses.size()).isEqualTo(2);
        assertThat(allCourses.get(0).getTitle()).isEqualTo(course.getTitle());
        assertThat(allCourses.get(1).getTitle()).isEqualTo(course2.getTitle());
    }

    @Transactional
    @Test
    void updateCourse_returnsUpdatedCourse() {
        // existing course
        Course course = new Course("test");
        Course savedCourse = courseRepository.save(course);
        //input for update
        CourseInputDto courseInputDto = new CourseInputDto("changedTest");
        //when
        CourseOutputDto updatedCourse = courseService.updateCourse(savedCourse.getId(), courseInputDto);

        //then
        assertThat(updatedCourse.getTitle()).isEqualTo(courseInputDto.getTitle());
    }

    @Test
    void updateCourse_throwsException_whenCourseNotFound() {

        CourseInputDto courseInputDto = new CourseInputDto("changedTest");
        assertThrows(Exception.class, () -> courseService.updateCourse(50L, courseInputDto));
    }

    @Transactional
    @Test
    void addModuleToCourse_returnsCourseWithAddedModule() {
        //given
        Course course = new Course("test");
        Course savedCourse = courseRepository.save(course);
        Module module = new Module("testModule");
        Module savedModule = moduleRepository.save(module);
        // when
        CourseOutputDto courseOutputDto = courseService.addModuleToCourse(savedCourse.getId(), savedModule.getId());
        //then
        assertThat(courseOutputDto.getTitle()).isEqualTo(savedCourse.getTitle());
        assertThat(savedCourse.getChildModules().size()).isEqualTo(1);
        assertThat(savedCourse.getChildModules().get(0).getTitle()).isEqualTo(savedModule.getTitle());
    }

    @Test
    void addModuleToCourse_throwsException_whenModuleNotFound() {
        Course course = new Course("test");
        Course savedCourse = courseRepository.save(course);

        assertThrows(Exception.class, () -> courseService.addModuleToCourse(course.getId(), 50L));
    }

    @Test
    void addModuleToCourse_throwsException_whenCourseNotFound() {
        Module module = new Module("testModule");
        Module savedModule = moduleRepository.save(module);

        assertThrows(Exception.class, () -> courseService.addModuleToCourse(50L, savedModule.getId()));
    }
}
