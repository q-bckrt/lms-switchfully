package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.SubmoduleService;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

import java.util.List;

/**
 * SubmoduleController handles HTTP requests related to submodules.
 * It allows coaches to create, retrieve, and update submodules.
 * Students can retrieve all submodules and view individual submodules.
 *
 * @see SubmoduleService
 */
@RestController
@RequestMapping("/submodules")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmoduleController {

    // FIELDS
    private final SubmoduleService submoduleService;

    // CONSTRUCTOR
    public SubmoduleController(SubmoduleService submoduleService) {
        this.submoduleService = submoduleService;
    }

    // METHODS

    /** Create a new submodule.
     * Only coaches can create submodules.
     *
     * @param submoduleInputDto the input data for the new submodule
     * @return the created submodule as a SubmoduleOutputDto
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmoduleOutputDto createSubmodule(@RequestBody SubmoduleInputDto submoduleInputDto) {
        return submoduleService.createSubmodule(submoduleInputDto);
    }

    /**
     * Get all submodules
     * Both students and coaches can retrieve all submodules.
     *
     * @return a list of all submodules as SubmoduleOutputDto
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<SubmoduleOutputDto> getAllSubmodules() {
        return submoduleService.getAllSubmodules();
    }

    /** Get a submodule by its ID.
     * Both students and coaches can retrieve a submodule by its ID.
     *
     * @param id the ID of the submodule to retrieve
     * @return the submodule as a SubmoduleOutputDto
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public SubmoduleOutputDto getSubmoduleById(@PathVariable Long id) {
        return submoduleService.getSubmoduleById(id);
    }

    /** Update a submodule by its ID.
     * Only coaches can update submodules.
     *
     * @param id the ID of the submodule to update
     * @param submoduleInputDto the input data for the updated submodule
     * @return the updated submodule as a SubmoduleOutputDto
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public SubmoduleOutputDto updateSubmodule(@PathVariable Long id, @RequestBody SubmoduleInputDto submoduleInputDto) {
        return submoduleService.updateSubmodule(id, submoduleInputDto);
    }

    /**
     * User get the percentage of Done for his/her submodule
     *
     * @param username username of the user who wants to see the submodule overview
     * @param submoduleId id of the submodule to get progress from
     * @return percentage of done
     */
    @GetMapping(path="/{submoduleId}/submodule-progress/{username}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public double getProgressPercentageSubmodule(@PathVariable String username,
                                              @PathVariable Long submoduleId){
        return submoduleService.getPercentageSubmoduleDoneForStudent(submoduleId, username);
    }

}
