package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Course;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;
import switchfully.lms.service.mapper.CourseMapper;

import java.util.List;

@Service
public class CourseService {

    // FIELDS
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    // CONSTRUCTORS
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    // METHODS
    public CourseOutputDto createCourse(CourseInputDto courseInputDto) {
        Course course = courseMapper.inputDtoToCourse(courseInputDto);
        Course saved = courseRepository.save(course);
        return courseMapper.courseToOutputDto(saved);
    }

    public CourseOutputDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        return courseMapper.courseToOutputDto(course);
    }

    public List<CourseOutputDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::courseToOutputDto)
                .toList();
    }

    public CourseOutputDto updateCourse(Long id, CourseInputDto courseInputDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        course.setTitle(courseInputDto.getTitle());
        Course updated = courseRepository.save(course);
        return courseMapper.courseToOutputDto(updated);
    }


}
