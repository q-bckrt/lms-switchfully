package switchfully.lms.service.dto;

import switchfully.lms.domain.User;

import java.util.List;
import java.util.Objects;

public class ClassOutputDto {
    private Long id;
    private String title;
    private List<User> users;
    public ClassOutputDto(Long id, String title, List<User> users) {
        this.id = id;
        this.title = title;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassOutputDto that = (ClassOutputDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, users);
    }

    @Override
    public String toString() {
        return "ClassOutputDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", users=" + users +
                '}';
    }
}
