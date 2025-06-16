package switchfully.lms.service.dto;

import lombok.Getter;

/**
 * Data Transfer Object for Course Input.
 * This class is used to transfer course data from the client to the server,
 * and is typically used in request bodies.
 */
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