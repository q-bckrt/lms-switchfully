package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.ModuleService;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;

import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    // FIELDS
    private final ModuleService moduleService;

    // CONSTRUCTOR
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // METHODS

    // Create
    //!\\ Front-End must get the ID of the created module from the response,
    // And use the addModule endpoint in CourseController to associate it with a course
    // because a module can never be "orphaned"
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleOutputDto createModule(@RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.createModule(moduleInputDto);
    }

    // Get All
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleOutputDto> getAllModules() {
        return moduleService.getAllModules();
    }

    // Get One By ID
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }

    // Edit (title)
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto updateModule(@PathVariable Long id, @RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.updateModule(id, moduleInputDto);
    }

    // Add Course By ID

    // Add Submodule By ID

    // Delete (by ID)
    // Get All Courses Associated (by module ID)
}
