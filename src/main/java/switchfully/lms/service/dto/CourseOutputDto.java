package switchfully.lms.service.dto;

import lombok.Setter;
import java.util.List;


@Setter
public class CourseOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private List<ModuleOutputDto> modules;

    // CONSTRUCTORS
    public CourseOutputDto() {}
    public CourseOutputDto(Long id, String title, List<ModuleOutputDto> modules) {
        this.id = id;
        this.title = title;
        this.modules = modules;
    }
}