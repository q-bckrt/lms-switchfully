package switchfully.lms.service.dto;

import java.util.List;
import java.util.Objects;

public class ClassOutputDtoList {
    private Long id;
    private String title;
    private CourseOutputDto course;
    private List<UserOutputDto> users;
    public ClassOutputDtoList(Long id, CourseOutputDto course, String title, List<UserOutputDto> users) {
        this.id = id;
        this.course = course;
        this.title = title;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public CourseOutputDto getCourse() {
        return course;
    }

    public String getTitle() {
        return title;
    }

    public List<UserOutputDto> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassOutputDtoList that = (ClassOutputDtoList) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(course, that.course) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, course, users);
    }

    @Override
    public String toString() {
        return "ClassOutputDtoList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", course=" + course +
                ", users=" + users +
                '}';
    }
}
