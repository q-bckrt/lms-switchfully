package switchfully.lms.service.dto;

import lombok.Getter;

@Getter
public class CodelabInputDto {

    // FIELDS
    private String title;
    private String details;

    // CONSTRUCTORS
    public CodelabInputDto() {}
    public CodelabInputDto(String title, String details) {
        this.title = title;
        this.details = details;
    }
}
