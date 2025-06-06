package switchfully.lms.service.dto;

import lombok.Data;
import java.util.List;


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

    @Override
    public String toString() {
        return id + " - " + title + " - " + childModules;
    }
}