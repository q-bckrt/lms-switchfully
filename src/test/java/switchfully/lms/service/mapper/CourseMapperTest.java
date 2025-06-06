package switchfully.lms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CourseMapperTest {

    private final CourseMapper courseMapper = new CourseMapper();
    private List<Module> childModules = new ArrayList<>();

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void cleanup(){
        courseRepository.deleteAll();
        courseRepository.flush();
    }

    @Test
    void givenValidCourseInputDto_whenInputDtoToCourse_thenReturnCourse(){
        //Given
        CourseInputDto courseInputDto = new CourseInputDto("test");
        //Expected
        Course expectedCourse = new Course("test");
        //when
        Course resultCourse = courseMapper.inputDtoToCourse(courseInputDto);
        //then
        assertThat(resultCourse).isEqualTo(expectedCourse);
        assertThat(resultCourse.getTitle()).isEqualTo(courseInputDto.getTitle());
    }

    @Test
    void givenValidCourseEntity_whenCourseToDto_thenReturnCourseOutputDto(){
        //given
        Course course = new Course("test");
        Course savedCourse = courseRepository.save(course);
        //expected
        CourseOutputDto expectedCourseOutputDto = new CourseOutputDto(1L,"test", childModules.stream().map(Module::getId).toList());
        //when
        CourseOutputDto resultCourseOutputDto = courseMapper.courseToOutputDto(savedCourse);
        //then
        assertThat(resultCourseOutputDto).isEqualTo(expectedCourseOutputDto);
        assertThat(resultCourseOutputDto.getTitle()).isEqualTo(expectedCourseOutputDto.getTitle());
    }
}
