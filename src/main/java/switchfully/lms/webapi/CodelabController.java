package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.CodelabService;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;

import java.util.List;

/**
 * REST controller for managing Codelabs.
 * Provides endpoints for creating, retrieving, and updating Codelabs.
 */
@RestController
@RequestMapping("/codelabs")
public class CodelabController {

    // FIELDS
    /**
     * Service for handling Codelab business logic.
     */
    private final CodelabService codelabService;

    // CONSTRUCTOR
    /**
     * Creates a new CodelabController with the specified service.
     *
     * @param codelabService Service for handling Codelab business logic
     */
    public CodelabController(CodelabService codelabService) {
        this.codelabService = codelabService;
    }

    // METHODS

    /**
     * Creates a new Codelab.
     * Only users with the COACH authority can access this endpoint.
     *
     * @param codelabInputDto The DTO containing the data for the new Codelab
     * @return A DTO containing the data of the created Codelab, including its generated ID
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public CodelabOutputDto createCodelab(@RequestBody CodelabInputDto codelabInputDto) {
        CodelabOutputDto codelabOutputDto = codelabService.createCodelab(codelabInputDto);
        System.out.println("Codelab DTO created with title: " + codelabOutputDto.getTitle());
        return codelabOutputDto;
    }

    /**
     * Retrieves all Codelabs.
     * Users with either STUDENT or COACH authority can access this endpoint.
     *
     * @return A list of DTOs containing the data of all Codelabs
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabService.getAllCodelabs();
    }

    /**
     * Retrieves a specific Codelab by its ID.
     * Users with either STUDENT or COACH authority can access this endpoint.
     *
     * @param id The ID of the Codelab to retrieve
     * @return A DTO containing the data of the requested Codelab
     * @throws IllegalArgumentException if no Codelab with the specified ID exists
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto getCodelabById(@PathVariable Long id) {
        return codelabService.getCodelabById(id);
    }

    /**
     * Updates an existing Codelab with the provided data.
     * Only users with the COACH authority can access this endpoint.
     *
     * @param id The ID of the Codelab to update
     * @param codelabInputDto The DTO containing the updated data
     * @return A DTO containing the data of the updated Codelab
     * @throws IllegalArgumentException if no Codelab with the specified ID exists
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto updateCodelab(@PathVariable Long id, @RequestBody CodelabInputDto codelabInputDto) {
        return codelabService.updateCodelab(id, codelabInputDto);
    }

}
