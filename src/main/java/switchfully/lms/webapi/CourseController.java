package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.CourseService;
import switchfully.lms.service.dto.CourseInputDto;
import switchfully.lms.service.dto.CourseOutputDto;

import java.util.List;

/**
 * CourseController handles HTTP requests related to courses.
 * It allows coaches to create, retrieve, and update courses.
 * Students can retrieve all courses and view individual courses.
 *
 * @see CourseService
 */
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

    /**
     * Create a new course.
     * Only coaches can create courses.
     *
     * @param courseInputDto the input data for the new course
     * @return the created course as a CourseOutputDto
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseOutputDto createCourse(@RequestBody CourseInputDto courseInputDto) {
        return courseService.createCourse(courseInputDto);
    }

    /**
     * Get all courses.
     * Both students and coaches can retrieve all courses.
     *
     * @return a list of all courses as CourseOutputDto
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseOutputDto> getAllCourses() {
        System.out.println("Hello CourseController");
        return courseService.getAllCourses();
    }

    /** Get a course by its ID.
     * Both students and coaches can retrieve a course by its ID.
     *
     * @param id the ID of the course to retrieve
     * @return the course as a CourseOutputDto
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CourseOutputDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    /** Edit (title) of a course.
     * Only coaches can update courses.
     *
     * @param id the ID of the course to update
     * @param courseInputDto the input data for the updated course
     * @return the updated course as a CourseOutputDto
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CourseOutputDto updateCourse(@PathVariable Long id, @RequestBody CourseInputDto courseInputDto) {
        return courseService.updateCourse(id, courseInputDto);
    }

    /** Add a module to a course.
     * Only coaches can add modules to courses.
     *
     * @param courseId the ID of the course to which the module will be added
     * @param moduleId the ID of the module to add
     * @return the updated course as a CourseOutputDto
     */
    @PutMapping(path = "/{courseId}/modules/{moduleId}", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CourseOutputDto addModuleToCourse(@PathVariable Long courseId, @PathVariable Long moduleId) {
        return courseService.addModuleToCourse(courseId, moduleId);
    }

    /**
     * User get the percentage of Done for his/her course
     *
     * @param username username of the user who wants to see the course overview
     * @param courseId id of the course to get progress from
     * @return percentage of done
     */
    @GetMapping(path="/{courseId}/course-progress/{username}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public double getProgressPercentageCourse(@PathVariable String username,
                                           @PathVariable Long courseId){
        return courseService.getPercentageCourseDoneForStudent(courseId, username);
    }


    // Delete (by ID) (not required)
    // Get All Modules Associated (by course ID) ??
}
