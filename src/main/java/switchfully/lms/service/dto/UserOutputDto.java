package switchfully.lms.service.dto;

import switchfully.lms.domain.Class;

import java.util.List;
import java.util.Objects;

public class UserOutputDto {

    private String userName;
    private String displayName;
    private String email;

    public UserOutputDto() {
    }

    public UserOutputDto(String userName, String displayName, String email) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserOutputDto that = (UserOutputDto) o;
        return Objects.equals(userName, that.userName) && Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, displayName);
    }

    @Override
    public String toString() {
        return "UserOutputDto{" +
                "userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
