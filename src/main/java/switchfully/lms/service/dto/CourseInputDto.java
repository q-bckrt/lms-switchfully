package switchfully.lms.service.dto;

import lombok.Getter;

@Getter
public class CourseInputDto {

    // FIELDS
    private String title;

    // CONSTRUCTOR
    public CourseInputDto() {}
    public CourseInputDto(String title) {
        this.title = title;
    }
}