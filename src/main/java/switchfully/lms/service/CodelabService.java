package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;
import switchfully.lms.service.mapper.CodelabMapper;

import java.util.List;

/**
 * Service class that handles business logic for Codelabs.
 * Provides methods for creating, retrieving, and updating Codelabs.
 */
@Service
public class CodelabService {

    // FIELDS
    /**
     * Repository for accessing Codelab data in the database.
     */
    private final CodelabRepository codelabRepository;

    /**
     * Mapper for converting between Codelab domain objects and DTOs.
     */
    private final CodelabMapper codelabMapper;

    // CONSTRUCTOR
    /**
     * Creates a new CodelabService with the specified repository and mapper.
     *
     * @param codelabRepository Repository for accessing Codelab data
     * @param codelabMapper Mapper for converting between Codelab domain objects and DTOs
     */
    public CodelabService(CodelabRepository codelabRepository, CodelabMapper codelabMapper) {
        this.codelabRepository = codelabRepository;
        this.codelabMapper = codelabMapper;
    }

    // METHODS
    /**
     * Creates a new Codelab based on the provided input data.
     * The Codelab is saved to the database and associated with its parent Submodule.
     *
     * @param codelabInputDto The DTO containing the data for the new Codelab
     * @return A DTO containing the data of the created Codelab, including its generated ID
     */
    public CodelabOutputDto createCodelab(CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabMapper.inputDtoToCodelab(codelabInputDto);
        System.out.println("Creating codelab: " + codelab.getTitle());
        Codelab saved = codelabRepository.save(codelab);
        System.out.println("Codelab saved with description: " + saved.getDetails());
        saved.getParentSubmodule().getChildCodelabs().add(saved);
        return codelabMapper.codelabToOutputDto(saved);
    }

    /**
     * Retrieves all Codelabs from the database.
     *
     * @return A list of DTOs containing the data of all Codelabs
     */
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabRepository
                .findAll()
                .stream()
                .map(codelabMapper::codelabToOutputDto)
                .toList();
    }

    /**
     * Retrieves a specific Codelab by its ID.
     *
     * @param id The ID of the Codelab to retrieve
     * @return A DTO containing the data of the requested Codelab
     * @throws IllegalArgumentException if no Codelab with the specified ID exists
     */
    public CodelabOutputDto getCodelabById(Long id) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        return codelabMapper.codelabToOutputDto(codelab);
    }

    /**
     * Updates an existing Codelab with the provided data.
     *
     * @param id The ID of the Codelab to update
     * @param codelabInputDto The DTO containing the updated data
     * @return A DTO containing the data of the updated Codelab
     * @throws IllegalArgumentException if no Codelab with the specified ID exists
     */
    public CodelabOutputDto updateCodelab(Long id, CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        codelab.setTitle(codelabInputDto.getTitle());
        codelab.setDetails(codelabInputDto.getDetails());
        Codelab updated = codelabRepository.save(codelab);
        return codelabMapper.codelabToOutputDto(updated);
    }

}
