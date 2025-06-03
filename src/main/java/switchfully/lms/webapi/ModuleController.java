package switchfully.lms.webapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import switchfully.lms.service.ModuleService;

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
    // Get All
    // Get One By ID
    // Edit (title)
    // Delete (by ID)
    // Get All Courses Associated (by module ID)
}
