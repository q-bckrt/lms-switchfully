package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Module;
import switchfully.lms.domain.Submodule;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

/**
 * Mapper class for converting between Submodule domain objects and DTOs.
 * Provides methods for converting a Submodule to a SubmoduleOutputDto and a SubmoduleInputDto to a Submodule.
 */
@Component
public class SubmoduleMapper {

    // METHODS
    /**
     * Converts a Submodule domain object to a SubmoduleOutputDto.
     *
     * @param submodule The Submodule domain object to convert
     * @return A DTO containing the data of the Submodule
     */
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

    /**
     * Converts a SubmoduleInputDto to a Submodule domain object.
     *
     * @param submoduleInputDto The DTO containing the data for the Submodule
     * @return A Submodule domain object with the data from the DTO
     */
    public Submodule inputDtoToSubmodule(SubmoduleInputDto submoduleInputDto) {
        return new Submodule(submoduleInputDto.getTitle());
    }
}
