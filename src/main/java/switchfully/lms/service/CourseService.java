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
    public CourseOutputDto createCourse(CourseInputDto courseInputDto) {
        Course course = courseMapper.inputDtoToCourse(courseInputDto);
        Course saved = courseRepository.save(course);
        return courseMapper.courseToOutputDto(saved);
    }

    // Needs more exception handling ?
    public CourseOutputDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        return courseMapper.courseToOutputDto(course);
    }

    public List<CourseOutputDto> getAllCourses() {
        return courseRepository
                .findAll()
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

    public CourseOutputDto addModuleToCourse(Long courseId, Long moduleId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + moduleId));
        course.addChildModule(module);
        Course updated = courseRepository.save(course);
        System.out.println(course.getChildModules());
        System.out.println(module.getParentCourses());
        return courseMapper.courseToOutputDto(updated);
    }

}