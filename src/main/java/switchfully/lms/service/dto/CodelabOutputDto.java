package switchfully.lms.service.dto;

import lombok.Data;

/**
 * Data Transfer Object for Codelab Output.
 * This class is used to transfer codelab data from the server to the client,
 * and is typically used in response bodies.
 */
@Data
public class CodelabOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private String details;
    private Long parentSubmoduleId;

    // CONSTRUCTORS
    public CodelabOutputDto() {}

    public CodelabOutputDto(Long id, String title, String details, Long parentSubmoduleId) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.parentSubmoduleId = parentSubmoduleId;
    }
}
