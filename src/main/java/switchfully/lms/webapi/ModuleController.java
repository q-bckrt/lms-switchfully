package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.ModuleService;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;

import java.util.List;

/**
 * ModuleController handles HTTP requests related to modules.
 * It allows coaches to create, retrieve, and update modules.
 * Students can retrieve all modules and view individual modules.
 *
 * @see ModuleService
 */
@RestController
@RequestMapping("/modules")
// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://lms-sw-frontend.netlify.app/")
public class ModuleController {

    // FIELDS
    private final ModuleService moduleService;

    // CONSTRUCTOR
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // METHODS

    /** Create a new module.
     * Only coaches can create modules.
     *
     * @param moduleInputDto the input data for the new module
     * @return the created module as a ModuleOutputDto
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleOutputDto createModule(@RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.createModule(moduleInputDto);
    }

    /** Get all modules.
     * Both students and coaches can retrieve all modules.
     *
     * @return a list of all modules as ModuleOutputDto
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleOutputDto> getAllModules() {
        return moduleService.getAllModules();
    }

    /** Get a module by its ID.
     * Both students and coaches can retrieve a module by its ID.
     *
     * @param id the ID of the module to retrieve
     * @return the module as a ModuleOutputDto
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }

    /** Update a module by its ID.
     * Only coaches can update modules.
     *
     * @param id the ID of the module to update
     * @param moduleInputDto the input data for the updated module
     * @return the updated module as a ModuleOutputDto
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto updateModule(@PathVariable Long id, @RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.updateModule(id, moduleInputDto);
    }

    /** Add a submodule to a module.
     * Only coaches can add submodules to modules.
     *
     * @param moduleId the ID of the module to which the submodule will be added
     * @param submoduleId the ID of the submodule to add
     * @return the updated module with the added submodule as a ModuleOutputDto
     */
    @PutMapping(path="/{moduleId}/submodules/{submoduleId}", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ModuleOutputDto subModule(@PathVariable Long moduleId, @PathVariable Long submoduleId) {
        return moduleService.addSubmoduleToModule(moduleId, submoduleId);
    }

    /**
     * User get the percentage of Done for his/her module
     *
     * @param username username of the user who wants to see the module overview
     * @param moduleId id of the module to get progress from
     * @return percentage of done
     */
    @GetMapping(path="/{moduleId}/module-progress/{username}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public double getProgressPercentageModule(@PathVariable String username,
                                              @PathVariable Long moduleId){
        return moduleService.getPercentageModuleDoneForStudent(moduleId, username);
    }

}
