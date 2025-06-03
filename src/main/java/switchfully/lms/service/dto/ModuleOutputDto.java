package switchfully.lms.service.dto;

import java.util.List;

public class ModuleOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private List<Long> parentCourses;
    private List<Long> childSubmodules;

    // CONSTRUCTORS
    public ModuleOutputDto() {}
    public ModuleOutputDto(Long id, String title, List<Long> parentCourses, List<Long> childSubmodules) {
        this.id = id;
        this.title = title;
        this.parentCourses = parentCourses;
        this.childSubmodules = childSubmodules;
    }
}