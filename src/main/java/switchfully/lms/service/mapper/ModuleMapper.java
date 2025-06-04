package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.domain.Module;

@Component
public class ModuleMapper {

    private final CourseRepository courseRepository;

    public ModuleMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // METHODS
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

    public Module inputDtoToModule(ModuleInputDto moduleInputDto) {
        return new Module(moduleInputDto.getTitle());
    }
}