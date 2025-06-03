package switchfully.lms.service.dto;

public class UserInputEditDto {

    private String userName;
    private String displayName;
    private String password;
    private String passwordControl;

    public UserInputEditDto() {
    }

    public UserInputEditDto(String userName, String displayName, String password, String passwordControl) {
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
        this.passwordControl = passwordControl;
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

    public String getPasswordControl() {
        return passwordControl;
    }
}
