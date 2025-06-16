package switchfully.lms.service.dto;

import lombok.Getter;

/**
 * Data Transfer Object for Submodule Input.
 * This class is used to transfer submodule data from the client to the server,
 * and is typically used in request bodies.
 */
@Getter
public class SubmoduleInputDto {

    // FIELDS
    private String title;

    // CONSTRUCTOR
    public SubmoduleInputDto() {}
    public SubmoduleInputDto(String title) {
        this.title = title;
    }
}
