package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

@Component
public class CourseMapper {

    // METHODS
    public CourseOutputDto courseToOutputDto(Course course) {
        return new CourseOutputDto(
                course.getId(),
                course.getTitle(),
                course
                        .getModules()
                        .stream()
                        .map(Module::getId)
                        .toList()
        );
    }

    public Course inputDtoToCourse(CourseInputDto courseInputDto) {
        return new Course(courseInputDto.getTitle());
    }
}
