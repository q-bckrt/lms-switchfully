package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.SubmoduleService;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

import java.util.List;

@RestController
@RequestMapping("/submodules")
public class SubmoduleController {

    // FIELDS
    private final SubmoduleService submoduleService;

    // CONSTRUCTOR
    public SubmoduleController(SubmoduleService submoduleService) {
        this.submoduleService = submoduleService;
    }

    // METHODS

    // Create
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmoduleOutputDto createSubmodule(@RequestBody SubmoduleInputDto submoduleInputDto) {
        return submoduleService.createSubmodule(submoduleInputDto);
    }

    // Get All
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<SubmoduleOutputDto> getAllSubmodules() {
        return submoduleService.getAllSubmodules();
    }

    // Get One By ID
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public SubmoduleOutputDto getSubmoduleById(@PathVariable Long id) {
        return submoduleService.getSubmoduleById(id);
    }

    // Edit (title)
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public SubmoduleOutputDto updateSubmodule(@PathVariable Long id, @RequestBody SubmoduleInputDto submoduleInputDto) {
        return submoduleService.updateSubmodule(id, submoduleInputDto);
    }

    // Delete (by ID) (not required)
    // Get All Modules Associated (by Submodule ID) ??
    // Get All Codelabs Associated (by Submodule ID) ??
}
