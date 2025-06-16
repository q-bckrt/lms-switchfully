package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;
import switchfully.lms.service.mapper.ModuleMapper;
import switchfully.lms.domain.Module;
import switchfully.lms.service.mapper.SubmoduleMapper;

import java.util.List;

/**
 * Service class for managing submodules.
 * Provides methods to create, retrieve, update, and manage submodules.
 */
@Service
public class SubmoduleService {

    // FIELDS
    private final SubmoduleRepository submoduleRepository;
    private final SubmoduleMapper submoduleMapper;
    private final CodelabRepository codelabsRepository;

    // CONSTRUCTOR
    public SubmoduleService(
            SubmoduleRepository submoduleRepository,
            SubmoduleMapper submoduleMapper,
            CodelabRepository codelabsRepository
    ) {
        this.submoduleRepository = submoduleRepository;
        this.submoduleMapper = submoduleMapper;
        this.codelabsRepository = codelabsRepository;
    }

    // METHODS
    /**
     * Creates a new submodule based on the provided input DTO.
     *
     * @param submoduleInputDto the input DTO containing submodule details
     * @return the created submodule as an output DTO
     */
    public SubmoduleOutputDto createSubmodule(SubmoduleInputDto submoduleInputDto) {
        Submodule submodule = submoduleMapper.inputDtoToSubmodule(submoduleInputDto);
        Submodule saved = submoduleRepository.save(submodule);
        return submoduleMapper.submoduleToOutputDto(saved);
    }

    /**
     * Retrieves all submodules.
     *
     * @return a list of all submodules as output DTOs
     */
    public List<SubmoduleOutputDto> getAllSubmodules() {
        return submoduleRepository
                .findAll()
                .stream()
                .map(submoduleMapper::submoduleToOutputDto)
                .toList();
    }

    /**
     * Retrieves a submodule by its ID.
     *
     * @param id the ID of the submodule to retrieve
     * @return the submodule as an output DTO
     * @throws IllegalArgumentException if no submodule is found with the given ID
     */
    public SubmoduleOutputDto getSubmoduleById(Long id) {
        Submodule submodule = submoduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submodule not found with id: " + id));
        return submoduleMapper.submoduleToOutputDto(submodule);
    }

    /**
     * Updates an existing submodule with the provided input DTO.
     *
     * @param id                 the ID of the submodule to update
     * @param submoduleInputDto  the input DTO containing updated submodule details
     * @return the updated submodule as an output DTO
     * @throws IllegalArgumentException if no submodule is found with the given ID
     */
    public SubmoduleOutputDto updateSubmodule(Long id, SubmoduleInputDto submoduleInputDto) {
        Submodule submodule = submoduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submodule not found with id: " + id));
        submodule.setTitle(submoduleInputDto.getTitle());
        Submodule updated = submoduleRepository.save(submodule);
        return submoduleMapper.submoduleToOutputDto(updated);
    }

}
