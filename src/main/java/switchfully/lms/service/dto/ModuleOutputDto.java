package switchfully.lms.service.dto;

import java.util.List;

public class ModuleOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private List<CourseOutputDto> courses;
    private List<SubmoduleOutputDto> submodules;

    // CONSTRUCTORS
    public ModuleOutputDto() {}
    public ModuleOutputDto(Long id, String title, List<CourseOutputDto> courses, List<SubmoduleOutputDto> submodules) {
        this.id = id;
        this.title = title;
        this.courses = courses;
        this.submodules = submodules;
    }
}