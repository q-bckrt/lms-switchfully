package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.User;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.ClassService;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.service.dto.ClassOutputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;

import java.util.List;

/**
 * REST controller that manages operations related to {@link Class} entities.
 * <p>
 * This controller exposes endpoints for:
 * <ul>
 *     <li>Creating a new class (authorized for {@code COACH} role only)</li>
 *     <li>Linking a {@link Course} to an existing class (authorized for {@code COACH} role only)</li>
 *     <li>Fetching all classes (public)</li>
 *     <li>Fetching a class by its ID (public)</li>
 * </ul>
 * <p>
 * Authorization is enforced via Spring Security using the {@link PreAuthorize} annotation. Roles are verified
 * using Keycloak tokens and checked for `COACH` authority.
 *
 * @see ClassService for the business logic associated with these endpoints
 * @see PreAuthorize for authorization annotations
 * @see Class for the domain entity this controller handles
 * @see Course for the course entity that can be linked to a class
 */
@RestController
@RequestMapping("/classes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClassController {

    private final ClassService classService;
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    /** endpoint to create and save a class to the database
     * @param classInputDto The payload with the information to create a class
     * @param coachUserName The userName of the logged in coach that wants to create a class
     * @see Class
     * @see User
     * @return ClassOutputDtoList object
     * */
    @PostMapping(path ="/{coachUserName}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassOutputDtoList createClass(@RequestBody ClassInputDto classInputDto,
                                          @PathVariable String coachUserName) {

        //AUTHORIZE
        return classService.createClass(classInputDto, coachUserName);
    }


    /** endpoint to link a course to a class
     * @param classId the id of the class that should be linked to the course
     * @param courseId the id of the course that should be linked to the class
     * @see Class
     * @see Course
     * @return ClassOutputDto object
     * */
    @PutMapping(path = "/linkCourseClass/{classId}/{courseId}", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto linkCourseToClass(@PathVariable Long classId,
                                            @PathVariable Long courseId) {
        //AUTHORIZE
        return classService.linkCourseToClass(classId, courseId);
    }

    /** endpoint to get all class entities in the database
     * @see Class
     * @return list of ClassOutputDto objects
     * */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDto> findAllClasses() {
        //AUTHORIZE
        return classService.findAllClasses();
    }

    /** endpoint to get a specific class entity from the database
     * @param classId the id of the class that should be returns
     * @see Class
     * @return list of ClassOutputDto objects
     * */
    @GetMapping(path = "/{classId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto findClassById(@PathVariable Long classId) {
        //AUTHORIZE
        return classService.findClassById(classId);
    }

    /** endpoint to get class list for specific username
     * @param username the id of the class that should be returns
     * @see Class
     * @return list of ClassOutputDto objects
     * */
    @GetMapping(path = "/get-classes/{username}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDto> findClassByUserName(@PathVariable String username) {
        //AUTHORIZE
        return classService.findClassForAUsername(username);
    }






}
