package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping(path ="/{coachUserName}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassOutputDtoList createClass(@RequestBody ClassInputDto classInputDto,
                                          @PathVariable String coachUserName) {

        //AUTHORIZE
        return classService.createClass(classInputDto, coachUserName);
    }


    @PutMapping(path = "/linkCourseClass/{classId}/{courseId}", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto linkCourseToClass(@PathVariable Long classId,
                                            @PathVariable Long courseId) {
        //AUTHORIZE
        return classService.linkCourseToClass(classId, courseId);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassOutputDto> findAllClasses() {
        //AUTHORIZE
        return classService.findAllClasses();
    }

    @GetMapping(path = "/{classId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ClassOutputDto findClassById(@PathVariable Long classId) {
        //AUTHORIZE
        return classService.findClassById(classId);
    }






}
