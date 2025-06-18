package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.*;
import switchfully.lms.domain.Module;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.repository.UserCodelabRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.service.mapper.ModuleMapper;

import java.util.List;

/**
 * Service class for managing modules.
 * Provides methods to create, retrieve, update, and manage modules and their associated submodules.
 */
@Service
public class ModuleService {

    // FIELDS
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final SubmoduleRepository submoduleRepository;
    private final UserRepository userRepository;
    private final UserCodelabRepository userCodelabRepository;

    // CONSTRUCTOR
    public ModuleService(
            ModuleRepository moduleRepository,
            ModuleMapper moduleMapper,
            SubmoduleRepository submoduleRepository,
            UserRepository userRepository,
            UserCodelabRepository userCodelabRepository
    ) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
        this.submoduleRepository = submoduleRepository;
        this.userRepository = userRepository;
        this.userCodelabRepository = userCodelabRepository;
    }

    // METHODS
    /**
     * Creates a new module based on the provided input DTO.
     *
     * @param moduleInputDto the input DTO containing module details
     * @return the created module as an output DTO
     */
    public ModuleOutputDto createModule(ModuleInputDto moduleInputDto) {
        Module module = moduleMapper.inputDtoToModule(moduleInputDto);
        Module saved = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(saved);
    }

    /**
     * Retrieves all modules.
     *
     * @return a list of all modules as output DTOs
     */
    public List<ModuleOutputDto> getAllModules() {
        return moduleRepository
                .findAll()
                .stream()
                .map(moduleMapper::moduleToOutputDto)
                .toList();
    }

    /**
     * Retrieves a module by its ID.
     *
     * @param id the ID of the module to retrieve
     * @return the module as an output DTO
     * @throws IllegalArgumentException if no module is found with the given ID
     */
    public ModuleOutputDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + id));
        return moduleMapper.moduleToOutputDto(module);
    }

    /**
     * Updates an existing module with the provided input DTO.
     *
     * @param id              the ID of the module to update
     * @param moduleInputDto  the input DTO containing updated module details
     * @return the updated module as an output DTO
     * @throws IllegalArgumentException if no module is found with the given ID
     */
    public ModuleOutputDto updateModule(Long id, ModuleInputDto moduleInputDto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + id));
        module.setTitle(moduleInputDto.getTitle());
        Module updated = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(updated);
    }

    /**
     * Adds a submodule to a module through its entity's method.
     *
     * @param moduleId   the ID of the module to which the submodule will be added
     * @param submoduleId the ID of the submodule to add
     * @return the updated module as an output DTO
     * @throws IllegalArgumentException if no module or submodule is found with the given IDs
     */
    public ModuleOutputDto addSubmoduleToModule(Long moduleId, Long submoduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + moduleId));
        Submodule submodule = submoduleRepository.findById(submoduleId)
                .orElseThrow(() -> new IllegalArgumentException("Submodule not found with id: " + submoduleId));
        module.addChildSubmodule(submodule);
        Module updated = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(updated);
    }

    /**
     * Get the percentage of done for a module for a specific user.
     *
     * @param moduleId                 the ID of the module to check
     * @param username  user for which we want the progress percentage
     * @return double percentage of done
     */
    public double getPercentageModuleDoneForStudent(Long moduleId, String username) {
        User user = userRepository.findByUserName(username);
        List<UserCodelab> userCodelabList = userCodelabRepository.findProgressByModuleIdAndUserID(moduleId, user.getId());

        if (userCodelabList == null || userCodelabList.isEmpty()) {
            return 0.0;
        }

        long doneCount = userCodelabList.stream()
                .filter(codelab -> codelab.getProgressLevel() == ProgressLevel.DONE)
                .count();

        return (doneCount * 100.0) / userCodelabList.size();

    }


}
