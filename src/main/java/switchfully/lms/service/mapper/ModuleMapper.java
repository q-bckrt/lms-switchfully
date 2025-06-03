package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.service.CourseService;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;
import switchfully.lms.domain.Module;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModuleMapper {

    private final CourseService courseService;

    public ModuleMapper(CourseService courseService) {
        this.courseService = courseService;
    }

    // METHODS
    /*
    public ModuleOutputDto moduleToOutputDto(Module module) {
        return new ModuleOutputDto(
                module.getId(),
                module.getTitle(),
                module
                        .getParentCourses()
                        .stream()
                        .map(course -> course.getId())
                        .toList(),
                module
                        .getChildSubmodules()
                        .stream()
                        .map(submodule -> submodule.getId())
                        .toList()
        );
    }
*/
}
