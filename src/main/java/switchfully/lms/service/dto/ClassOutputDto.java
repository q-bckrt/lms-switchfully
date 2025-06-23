package switchfully.lms.service.dto;

import java.util.List;
import java.util.Objects;

public class ClassOutputDto {
    private Long id;
    private String title;
    private Long courseId;
    private String courseTitle;

    public ClassOutputDto(Long id, String title, Long courseId, String courseTitle) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public Long getCourseId() {
        return courseId;
    }
    public String getCourseTitle() {
        return courseTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassOutputDto that = (ClassOutputDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "ClassOutputDto{" +
                "title='" + title + '\'' +
                ", id=" + id + '\'' +
                '}';
    }
}
