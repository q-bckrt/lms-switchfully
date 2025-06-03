package switchfully.lms.service.dto;

import lombok.Getter;

@Getter
public class ModuleInputDto {

// FIELDS
    private String title;
    // a module must be associated with one course when created.
    // later, it can be associated with multiple courses.
    private Long courseId;

    // CONSTRUCTOR
    public ModuleInputDto() {}
    public ModuleInputDto(String title, Long courseId) {
        this.title = title;
        this.courseId = courseId;
    }
}
