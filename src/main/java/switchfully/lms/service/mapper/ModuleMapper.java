package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Submodule;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.domain.Module;

/**
 * Mapper class for converting between Module domain objects and DTOs.
 * Provides methods for converting a Module to a ModuleOutputDto and a ModuleInputDto to a Module.
 */
@Component
public class ModuleMapper {

    // METHODS
    /**
     * Converts a Module domain object to a ModuleOutputDto.
     *
     * @param module The Module domain object to convert
     * @return A DTO containing the data of the Module
     */
    public ModuleOutputDto moduleToOutputDto(Module module) {
        return new ModuleOutputDto(
                module.getId(),
                module.getTitle(),
                module
                        .getParentCourses()
                        .stream()
                        .map(Course::getId)
                        .toList(),
                module
                        .getChildSubmodules()
                        .stream()
                        .map(Submodule::getId)
                        .toList()
        );
    }

    /**
     * Converts a ModuleInputDto to a Module domain object.
     *
     * @param moduleInputDto The DTO containing the data for the Module
     * @return A Module domain object with the data from the DTO
     */
    public Module inputDtoToModule(ModuleInputDto moduleInputDto) {
        return new Module(moduleInputDto.getTitle());
    }
}