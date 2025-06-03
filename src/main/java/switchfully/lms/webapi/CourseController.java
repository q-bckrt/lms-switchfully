package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import switchfully.lms.service.CourseService;

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
    // Get All
    // Get One By ID
    // Edit (title)
    // Delete (by ID)
    // Get All Modules (by course ID)
}
