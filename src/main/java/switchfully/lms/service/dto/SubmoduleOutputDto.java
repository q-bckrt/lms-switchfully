package switchfully.lms.service.dto;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for Submodule Output.
 * This class is used to transfer submodule data from the server to the client,
 * and is typically used in response bodies.
 */
@Data
public class SubmoduleOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private List<Long> parentModules;
    private List<Long> childCodelabs;

    // CONSTRUCTORS
    public SubmoduleOutputDto() {}
    public SubmoduleOutputDto(Long id, String title, List<Long> parentModules, List<Long> childCodelabs) {
        this.id = id;
        this.title = title;
        this.parentModules = parentModules;
        this.childCodelabs = childCodelabs;
    }
}
