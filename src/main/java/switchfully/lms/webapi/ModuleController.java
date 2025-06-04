package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.ModuleService;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;

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
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleOutputDto createModule(@RequestBody ModuleInputDto moduleInputDto) {
        return moduleService.createModule(moduleInputDto);
    }



    // Get All
    // Get One By ID
    // Edit (title)
    // Delete (by ID)
    // Get All Courses Associated (by module ID)
}
