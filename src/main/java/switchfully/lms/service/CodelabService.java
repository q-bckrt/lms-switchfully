package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;
import switchfully.lms.service.mapper.CodelabMapper;

import java.util.List;

/**
 * Service class for managing codelabs.
 * Provides methods to create, retrieve, update, and manage codelabs.
 */
@Service
public class CodelabService {

    // FIELDS
    private final CodelabRepository codelabRepository;

    private final CodelabMapper codelabMapper;

    // CONSTRUCTOR
    public CodelabService(CodelabRepository codelabRepository, CodelabMapper codelabMapper) {
        this.codelabRepository = codelabRepository;
        this.codelabMapper = codelabMapper;
    }

    // METHODS
    /**
     * Creates a new codelab based on the provided input DTO, and associates it with the parent submodule
     * that is specified in the input DTO.
     *
     * @param codelabInputDto the input DTO containing codelab details
     * @return the created codelab as an output DTO
     */
    public CodelabOutputDto createCodelab(CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabMapper.inputDtoToCodelab(codelabInputDto);
        Codelab saved = codelabRepository.save(codelab);
        saved.getParentSubmodule().getChildCodelabs().add(saved);
        return codelabMapper.codelabToOutputDto(saved);
    }

    /**
     * Retrieves all codelabs.
     *
     * @return a list of all codelabs as output DTOs
     */
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabRepository
                .findAll()
                .stream()
                .map(codelabMapper::codelabToOutputDto)
                .toList();
    }

    /**
     * Retrieves a codelab by its ID.
     *
     * @param id the ID of the codelab to retrieve
     * @return the codelab as an output DTO
     * @throws IllegalArgumentException if no codelab is found with the given ID
     */
    public CodelabOutputDto getCodelabById(Long id) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        return codelabMapper.codelabToOutputDto(codelab);
    }

    /**
     * Updates an existing codelab with the provided input DTO.
     *
     * @param id              the ID of the codelab to update
     * @param codelabInputDto the input DTO containing updated codelab details
     * @return the updated codelab as an output DTO
     * @throws IllegalArgumentException if no codelab is found with the given ID
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
