package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.CodelabService;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;

import java.util.List;

@RestController
@RequestMapping("/codelabs")
public class CodelabController {

    // FIELDS
    private final CodelabService codelabService;

    // CONSTRUCTOR
    public CodelabController(CodelabService codelabService) {
        this.codelabService = codelabService;
    }

    // METHODS

    // Create
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CodelabOutputDto createCodelab(@RequestBody CodelabInputDto codelabInputDto) {
        CodelabOutputDto codelabOutputDto = codelabService.createCodelab(codelabInputDto);
        System.out.println("Codelab DTO created with title: " + codelabOutputDto.getTitle());
        return codelabOutputDto;
    }

    // Get All
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabService.getAllCodelabs();
    }

    // Get One By ID
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto getCodelabById(@PathVariable Long id) {
        return codelabService.getCodelabById(id);
    }

    // Edit (title, details)
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto updateCodelab(@PathVariable Long id, @RequestBody CodelabInputDto codelabInputDto) {
        return codelabService.updateCodelab(id, codelabInputDto);
    }

}
