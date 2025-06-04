package switchfully.lms.service.dto;

import lombok.Getter;

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
