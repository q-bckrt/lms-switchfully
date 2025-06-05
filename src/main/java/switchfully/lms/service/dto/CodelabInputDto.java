package switchfully.lms.service.dto;

import lombok.Getter;

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