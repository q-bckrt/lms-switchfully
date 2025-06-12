package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.UserService;
import switchfully.lms.service.dto.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDto registerAsStudent(@RequestBody UserInputDto userInputDto) {
        return userService.createNewStudent(userInputDto);
    }

    @GetMapping(path="/{username}",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList getProfileInfo(@PathVariable String username) {
        return userService.getProfile(username);
    }

    @PutMapping(path="/{username}/edit",consumes = "application/json",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList updateProfileInfo(@PathVariable String username,
                                               @RequestBody UserInputEditDto userInputEditDto) {
        return userService.updateProfile(userInputEditDto,username);
    }

    @PutMapping(path="/{username}/edit/class",produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDtoList updateClassInfo(@PathVariable String username,
                                             @RequestParam (name = "classId", required = true) Long classId) {
        return userService.updateClassInfo(classId,username);
    }

    @GetMapping(path = "/{userName}/class-overview", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDtoList getClassOverview(@PathVariable String userName) {
        //AUTHORIZE
        return userService.getClassOverview(userName);
    }

}
