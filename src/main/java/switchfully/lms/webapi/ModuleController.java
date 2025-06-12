package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleOutputDto createModule(@RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.createModule(moduleInputDto);
    }

    // Get All
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleOutputDto> getAllModules() {
        return moduleService.getAllModules();
    }

    // Get One By ID
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }

    // Edit (title)
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto updateModule(@PathVariable Long id, @RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.updateModule(id, moduleInputDto);
    }

    // Add Submodule By ID
    @PutMapping(path="/{moduleId}/submodules/{submoduleId}", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto subModule(@PathVariable Long moduleId, @PathVariable Long submoduleId) {
        return moduleService.addSubmoduleToModule(moduleId, submoduleId);
    }

    // Delete (by ID) (not required)
    // Get All Courses Associated (by module ID) ??
    // Get All Submodules Associated (by module ID) ??
}
