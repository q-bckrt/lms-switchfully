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
    public ClassController(ClassService classService) {
        this.classService = classService;
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

    @PutMapping(path = "/linkCourseClass/{classId}/{courseId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto linkCourseToClass(@PathVariable Long classId,
                                            @PathVariable Long courseId) {
        return classService.linkCourseToClass(classId, courseId);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDto> findAllClasses() {
        return classService.findAllClasses();
    }

    @GetMapping(path = "/{classId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto findClassById(@PathVariable Long classId) {

        return classService.findClassById(classId);
    }






}
