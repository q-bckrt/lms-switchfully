package switchfully.lms.service.dto;

import switchfully.lms.domain.Class;

import java.util.List;
import java.util.Objects;

public class UserOutputDtoList {

    private String userName;
    private String displayName;
    private String email;
    private List<ClassOutputDto> classes;

    public UserOutputDtoList() {
    }

    public UserOutputDtoList(String userName, String displayName, String email,List<ClassOutputDto> classes) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.classes = classes;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getEmail() {
        return email;
    }

    public List<ClassOutputDto> getClasses() {
        return classes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserOutputDtoList that = (UserOutputDtoList) o;
        return Objects.equals(userName, that.userName) && Objects.equals(displayName, that.displayName) && Objects.equals(classes, that.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, displayName, classes);
    }

    @Override
    public String toString() {
        return "UserOutputDtoList{" +
                "userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", classes=" + classes +
                '}';
    }
}
