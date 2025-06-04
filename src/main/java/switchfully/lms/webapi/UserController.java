package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.UserService;
import switchfully.lms.service.dto.UserInputDto;
import switchfully.lms.service.dto.UserInputEditDto;
import switchfully.lms.service.dto.UserOutputDtoList;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

//    @PostMapping(consumes = "application/json", produces = "application/json")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserOutputDtoList registerAsStudent(@RequestBody UserInputDto userInputDto) {
//        return userService.createNewStudent(userInputDto);
//    }
//
//    @GetMapping(path="/{username}",produces = "application/json")
//    @ResponseStatus(HttpStatus.OK)
//    public UserOutputDtoList getProfileInfo(@PathVariable String username) {
//        return userService.getProfile(username);
//    }
//
//    @PutMapping(path="/{username}/edit",consumes = "application/json",produces = "application/json")
//    @ResponseStatus(HttpStatus.OK)
//    public UserOutputDtoList updateProfileInfo(@PathVariable String username,
//                                               @RequestBody UserInputEditDto userInputEditDto) {
//        return userService.updateProfile(userInputEditDto,username);
//    }

}
