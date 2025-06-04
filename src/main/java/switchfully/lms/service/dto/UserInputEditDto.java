package switchfully.lms.service.dto;

public class UserInputEditDto {

    private String userName;
    private String displayName;
    private String password;

    public UserInputEditDto() {
    }

    public UserInputEditDto(String userName, String displayName, String password) {
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

}
