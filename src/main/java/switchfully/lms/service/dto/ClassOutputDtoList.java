package switchfully.lms.service.dto;

import java.util.List;
import java.util.Objects;

public class ClassOutputDtoList {
    private Long id;
    private String title;
    private List<UserOutputDto> users;
    public ClassOutputDtoList(Long id, String title, List<UserOutputDto> users) {
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

    public List<UserOutputDto> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassOutputDtoList that = (ClassOutputDtoList) o;
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
