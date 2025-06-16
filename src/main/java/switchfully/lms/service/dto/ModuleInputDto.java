package switchfully.lms.service.dto;

import lombok.Getter;

/**
 * Data Transfer Object for Module Input.
 * This class is used to transfer module data from the client to the server,
 * and is typically used in request bodies.
 */
@Getter
public class ModuleInputDto {

// FIELDS
    private String title;

    // CONSTRUCTOR
    public ModuleInputDto() {}
    public ModuleInputDto(String title) {
        this.title = title;
    }


}
