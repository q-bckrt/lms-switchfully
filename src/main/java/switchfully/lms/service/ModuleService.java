package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.service.mapper.ModuleMapper;
import switchfully.lms.domain.Module;

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

}
