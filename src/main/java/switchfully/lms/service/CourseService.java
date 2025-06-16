package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Course;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;
import switchfully.lms.service.mapper.CourseMapper;
import switchfully.lms.domain.Module;

import java.util.List;

/**
 * Service class for managing courses.
 * Provides methods to create, retrieve, update, and manage courses and their associated modules.
 */
@Service
public class CourseService {

    // FIELDS
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ModuleRepository moduleRepository;

    // CONSTRUCTORS
    public CourseService(
            CourseRepository courseRepository,
            CourseMapper courseMapper,
            ModuleRepository moduleRepository
    ) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.moduleRepository = moduleRepository;
    }

    // METHODS
    /**
     * Creates a new course based on the provided input DTO.
     *
     * @param courseInputDto the input DTO containing course details
     * @return the created course as an output DTO
     */
    public CourseOutputDto createCourse(CourseInputDto courseInputDto) {
        Course course = courseMapper.inputDtoToCourse(courseInputDto);
        Course saved = courseRepository.save(course);
        return courseMapper.courseToOutputDto(saved);
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param id the ID of the course to retrieve
     * @return the course as an output DTO
     * @throws IllegalArgumentException if no course is found with the given ID
     */
    public CourseOutputDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        return courseMapper.courseToOutputDto(course);
    }

    /**
     * Retrieves all courses.
     *
     * @return a list of all courses as output DTOs
     */
    public List<CourseOutputDto> getAllCourses() {
        return courseRepository
                .findAll()
                .stream()
                .map(courseMapper::courseToOutputDto)
                .toList();
    }

    /**
     * Updates an existing course with the provided input DTO.
     *
     * @param id              the ID of the course to update
     * @param courseInputDto  the input DTO containing updated course details
     * @return the updated course as an output DTO
     * @throws IllegalArgumentException if no course is found with the given ID
     */
    public CourseOutputDto updateCourse(Long id, CourseInputDto courseInputDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        course.setTitle(courseInputDto.getTitle());
        Course updated = courseRepository.save(course);
        return courseMapper.courseToOutputDto(updated);
    }

    /**
     * Adds a module to a course through its entity's method.
     *
     * @param courseId
     * @param moduleId
     * @return
     */
    public CourseOutputDto addModuleToCourse(Long courseId, Long moduleId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + moduleId));
        course.addChildModule(module);
        Course updated = courseRepository.save(course);
        return courseMapper.courseToOutputDto(updated);
    }

}