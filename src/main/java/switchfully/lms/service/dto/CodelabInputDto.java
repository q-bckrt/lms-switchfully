package switchfully.lms.service.dto;

import lombok.Getter;

/**
 * Data Transfer Object for Codelab Input.
 * This class is used to transfer codelab data from the client to the server,
 * and is typically used in request bodies.
 */
@Getter
public class CodelabInputDto {

    // FIELDS
    private String title;
    private String details;
    private Long parentSubmoduleId;

    // CONSTRUCTORS
    public CodelabInputDto() {}
    public CodelabInputDto(String title, String details, Long parentSubmoduleId) {
        this.title = title;
        this.details = details;
        this.parentSubmoduleId = parentSubmoduleId;
    }
}