package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.service.mapper.ModuleMapper;
import switchfully.lms.domain.Module;

import java.util.List;

@Service
public class ModuleService {

    // FIELDS
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final SubmoduleRepository submoduleRepository;

    // CONSTRUCTOR
    public ModuleService(
            ModuleRepository moduleRepository,
            ModuleMapper moduleMapper,
            SubmoduleRepository submoduleRepository
    ) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
        this.submoduleRepository = submoduleRepository;
    }

    // METHODS
    public ModuleOutputDto createModule(ModuleInputDto moduleInputDto) {
        Module module = moduleMapper.inputDtoToModule(moduleInputDto);
        Module saved = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(saved);
    }

    // Needs more exception handling ?
    public List<ModuleOutputDto> getAllModules() {
        return moduleRepository
                .findAll()
                .stream()
                .map(moduleMapper::moduleToOutputDto)
                .toList();
    }

    public ModuleOutputDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + id));
        return moduleMapper.moduleToOutputDto(module);
    }

    public ModuleOutputDto updateModule(Long id, ModuleInputDto moduleInputDto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + id));
        module.setTitle(moduleInputDto.getTitle());
        Module updated = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(updated);
    }

    public ModuleOutputDto addSubmoduleToModule(Long moduleId, Long submoduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found with id: " + moduleId));
        Submodule submodule = submoduleRepository.findById(submoduleId)
                .orElseThrow(() -> new IllegalArgumentException("Submodule not found with id: " + submoduleId));
        module.addChildSubmodule(submodule);
        Module updated = moduleRepository.save(module);
        return moduleMapper.moduleToOutputDto(updated);
    }


}
