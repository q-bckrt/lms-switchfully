package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.domain.User;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.ClassService;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.service.dto.ClassOutputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final ClassService classService;
    private final UserRepository userRepository; // THIS NEEDS TO BE REPLACED WITH AUTH SERVICE!!!!
    public ClassController(ClassService classService, UserRepository userRepository) {
        this.classService = classService;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassOutputDtoList createClass(@RequestBody ClassInputDto classInputDto,
                                          User coach) {

        //AUTHORIZATION SERVICE -> AUTHORIZE WITH TOKEN AND GET USER COACH
        return classService.createClass(classInputDto, coach);
    }

    @GetMapping(path = "/classOverview", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDtoList getClassOverview(@RequestParam(name = "classId", required = true) Long classId,
                                               User user) {

        //AUTHORIZATION SERVICE -> AUTHORIZE WITH TOKEN AND GET USER

        return classService.getClassOverview(classId, user);
    }

    //The 2 end points below are now setup to ony be used by a coach, not really sure if that is okay for front end
    // ALSO getclassoverview is very similar to findclass by id, only difference is that the overview only returns the dto if
    // the requestee is part of the class... maybe just consilidate into 1 multi-purpose method? of even some logic here to check
    // if the requestee is part of the class user list?

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDto> findAllClasses() {

        //AUTHORIZATION SERVICE -> AUTHORIZE WITH TOKEN AND GET USER COACH
        return classService.findAllClasses();
    }

    @GetMapping(path = "/{classId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDtoList findClassById(@PathVariable Long classId) {

        //AUTHORIZATION SERVICE -> AUTHORIZE WITH TOKEN AND GET USER COACH
        return classService.findClassById(classId);
    }






}
