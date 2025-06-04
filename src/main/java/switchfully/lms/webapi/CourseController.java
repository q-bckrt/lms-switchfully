package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.CourseService;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    // FIELDS
    private final CourseService courseService;

    // CONSTRUCTOR
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // METHODS

    // Create
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseOutputDto createCourse(@RequestBody CourseInputDto courseInputDto) {
        return courseService.createCourse(courseInputDto);
    }
    // Get All
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseOutputDto> getAllCourses() {
        System.out.println("Hello CourseController");
        return courseService.getAllCourses();
    }

    // Get One By ID
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CourseOutputDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }
    // Edit (title)
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CourseOutputDto updateCourse(@PathVariable Long id, @RequestBody CourseInputDto courseInputDto) {
        return courseService.updateCourse(id, courseInputDto);
    }

    // Add Module By ID
    @PostMapping(path = "/{courseId}/modules/{moduleId}", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseOutputDto addModuleToCourse(@PathVariable Long courseId, @PathVariable Long moduleId) {
        return courseService.addModuleToCourse(courseId, moduleId);
    }

    // Delete (by ID)
    // Get All Modules (by course ID)
}
