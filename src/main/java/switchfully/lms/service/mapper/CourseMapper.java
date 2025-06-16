package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

/**
 * Mapper class for converting between Course domain objects and DTOs.
 * Provides methods for converting a Course to a CourseOutputDto and a CourseInputDto to a Course.
 */
@Component
public class CourseMapper {

    // METHODS
    /**
     * Converts a Course domain object to a CourseOutputDto.
     *
     * @param course The Course domain object to convert
     * @return A DTO containing the data of the Course
     */
    public CourseOutputDto courseToOutputDto(Course course) {
        return new CourseOutputDto(
                course.getId(),
                course.getTitle(),
                course
                        .getChildModules()
                        .stream()
                        .map(Module::getId)
                        .toList()
        );
    }

    /**
     * Converts a CourseInputDto to a Course domain object.
     *
     * @param courseInputDto The DTO containing the data for the Course
     * @return A Course domain object with the data from the DTO
     */
    public Course inputDtoToCourse(CourseInputDto courseInputDto) {
        return new Course(courseInputDto.getTitle());
    }
}
