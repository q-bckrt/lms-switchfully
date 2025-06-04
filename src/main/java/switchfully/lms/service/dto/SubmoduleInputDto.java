package switchfully.lms.service.dto;

import lombok.Getter;

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
