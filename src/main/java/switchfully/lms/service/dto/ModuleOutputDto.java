package switchfully.lms.service.dto;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for Module Output.
 * This class is used to transfer module data from the server to the client,
 * and is typically used in response bodies.
 */
@Data
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