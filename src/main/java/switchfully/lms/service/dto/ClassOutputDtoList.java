package switchfully.lms.service.dto;

import java.util.List;
import java.util.Objects;

public class ClassOutputDtoList {
    private String title;
    private List<UserOutputDto> users;
    public ClassOutputDtoList(String title, List<UserOutputDto> users) {
        this.title = title;
        this.users = users;
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
        return Objects.equals(title, that.title) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, users);
    }

    @Override
    public String toString() {
        return "ClassOutputDto{" +
                ", title='" + title + '\'' +
                ", users=" + users +
                '}';
    }
}
