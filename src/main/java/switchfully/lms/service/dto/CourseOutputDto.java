package switchfully.lms.service.dto;

import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object for Course Output.
 * This class is used to transfer course data from the server to the client,
 * and is typically used in response bodies.
 */
@Data
public class CourseOutputDto {

    // FIELDS
    private Long id;
    private String title;
    private List<Long> childModules;

    // CONSTRUCTORS
    public CourseOutputDto() {}
    public CourseOutputDto(Long id, String title, List<Long> childModules) {
        this.id = id;
        this.title = title;
        this.childModules = childModules;
    }
}