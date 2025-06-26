package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.UserService;
import switchfully.lms.service.dto.*;

import java.util.List;

/**
 * REST controller for managing user-related operations such as registration, profile updates, and class info retrieval.
 * <p>
 * Handles incoming HTTP requests and delegates processing to {@link UserService}.
 */
// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://lms-sw-frontend.netlify.app/")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Registers a new student user.
     *
     * @param userInputDto DTO containing the student's registration information
     * @return the created user details
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDto registerAsStudent(@RequestBody UserInputDto userInputDto) {
        return userService.createNewStudent(userInputDto);
    }

    /**
     * Get information for the profile page.
     *
     * @param username username of the user we need to profile info for
     * @return username, display name, email and list of classes
     */
    @GetMapping(path="/{username}",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList getProfileInfo(@PathVariable String username) {
        return userService.getProfile(username);
    }

    /**
     * Edit profile information, only the password and the display name can be changed.
     *
     * @param username username of user who wants to change its profile info,
     * @param userInputEditDto dto with info that need to be changed
     * @return updated profile info (only the display name is returned, for security reasons)
     */
    @PutMapping(path="/{username}/edit",consumes = "application/json",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList updateProfileInfo(@PathVariable String username,
                                               @RequestBody UserInputEditDto userInputEditDto) {
        System.out.println(userInputEditDto.getDisplayName());
        System.out.println(userInputEditDto.getPassword());
        return userService.updateProfile(userInputEditDto,username);
    }

    /**
     * Add class to which the user belongs to. <p>
     *
     * Only one class can be added at a time.
     * @param username username of user who wants to change its profile info,
     * @param classId ID of the class that need to be added
     * @return update profile info
     */
    @PutMapping(path="/{username}/edit/class",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList updateClassInfo(@PathVariable String username,
                                             @RequestParam (name = "classId", required = true) Long classId) {
        return userService.updateClassInfo(classId,username);
    }

    /**
     * User get the overview of the class(es) he belongs to.
     *
     * @param username username of the user who wants to see the class overview
     * @return the class overview
     */
    @GetMapping(path = "/{username}/class-overview", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDtoList> getClassOverview(@PathVariable String username) {
        //AUTHORIZE
        return userService.getClassOverview(username);
    }

    /**
     * User get the overview of all of his codelabs.
     *
     * @param username username of the user who wants to see the class overview
     * @return the class overview
     */
    @GetMapping(path = "/{username}/codelabs-progress-overview", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ProgressPerUserDtoList getCodelabProgressPerUser(@PathVariable String username) {
        //AUTHORIZE
        return userService.getCodelabProgressPerUser(username);
    }


    /**
     * User get the overview of all of his codelabs.
     *
     * @param username username of the user who wants to see the class overview
     * @return the class overview
     */
    @PutMapping(path="/{username}/edit/codelab/{codelabId}",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    //only student or student and coach?
    @ResponseStatus(HttpStatus.OK)
    public Boolean progressLevel(@PathVariable String username,
                                 @PathVariable Long codelabId,
                                 @RequestParam (name = "progressLevel", required = true) String progressLevel) {
        return userService.updateProgressLevel(username,codelabId,progressLevel);
    }

    /**
     * User get the overview of all of his codelabs.
     *
     * @param username username of the user who wants to see the class overview
     * @return the class overview
     */
    @GetMapping(path = "/{username}/all-students-overview", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public OverviewProgressCoachDto getOverviewStudentForCoach(@PathVariable String username) {
        //AUTHORIZE
        return userService.getOverviewCoach(username);
    }


}
