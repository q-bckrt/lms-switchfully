package switchfully.lms.service.dto;

import lombok.Data;

import java.util.List;

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
