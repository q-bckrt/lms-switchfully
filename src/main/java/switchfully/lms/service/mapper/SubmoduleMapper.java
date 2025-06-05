package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Module;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

@Component
public class SubmoduleMapper {

    private final ModuleRepository moduleRepository;

    public SubmoduleMapper(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // METHODS
    public SubmoduleOutputDto submoduleToOutputDto(Submodule submodule) {
        return new SubmoduleOutputDto(
                submodule.getId(),
                submodule.getTitle(),
                submodule.getParentModules()
                        .stream()
                        .map(Module::getId)
                        .toList(),
                submodule
                        .getChildCodelabs()
                        .stream()
                        .map(Codelab::getId)
                        .toList()
        );
    }

    public Submodule inputDtoToSubmodule(SubmoduleInputDto submoduleInputDto) {
        return new Submodule(submoduleInputDto.getTitle());
    }
}
