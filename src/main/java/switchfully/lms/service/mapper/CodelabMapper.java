package switchfully.lms.service.mapper;

import switchfully.lms.domain.Codelab;
import org.springframework.stereotype.Component;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.CodelabOutputDto;
import switchfully.lms.service.dto.CodelabInputDto;

/**
 * Mapper class for converting between Codelab domain objects and DTOs.
 * Provides methods for converting a Codelab to a CodelabOutputDto and a CodelabInputDto to a Codelab.
 */
@Component
public class CodelabMapper {

    // FIELDS
    /**
     * Repository for accessing Submodule data in the database.
     * Used to find the parent Submodule when converting from a DTO to a domain object.
     */
    private final SubmoduleRepository submoduleRepository;

    // CONSTRUCTOR
    /**
     * Creates a new CodelabMapper with the specified repository.
     *
     * @param submoduleRepository Repository for accessing Submodule data
     */
    public CodelabMapper(SubmoduleRepository submoduleRepository) {
        this.submoduleRepository = submoduleRepository;
    }

    // METHODS
    /**
     * Converts a Codelab domain object to a CodelabOutputDto.
     *
     * @param codelab The Codelab domain object to convert
     * @return A DTO containing the data of the Codelab
     */
    public CodelabOutputDto codelabToOutputDto(Codelab codelab) {
        return new CodelabOutputDto(
                codelab.getId(),
                codelab.getTitle(),
                codelab.getDetails(),
                codelab.getParentSubmodule().getId()
        );
    }

    /**
     * Converts a CodelabInputDto to a Codelab domain object.
     * Finds the parent Submodule using the SubmoduleRepository.
     *
     * @param codelabInputDto The DTO containing the data for the Codelab
     * @return A Codelab domain object with the data from the DTO
     * @throws IllegalArgumentException if the parent Submodule is not found
     */
    public Codelab inputDtoToCodelab(CodelabInputDto codelabInputDto) {
        Submodule parentSubmodule = submoduleRepository.findById(codelabInputDto.getParentSubmoduleId())
                .orElseThrow(() -> new IllegalArgumentException("Parent submodule not found with id: " + codelabInputDto.getParentSubmoduleId()));
        return new Codelab(
                codelabInputDto.getTitle(),
                codelabInputDto.getDetails(),
                parentSubmodule);
    }
}
