package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.repository.ModuleRepository;
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

    // CONSTRUCTOR
    public ModuleService(
            ModuleRepository moduleRepository,
            ModuleMapper moduleMapper
    ) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
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



}
