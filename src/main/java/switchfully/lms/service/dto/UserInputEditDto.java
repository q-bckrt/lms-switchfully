package switchfully.lms.service.dto;

public class UserInputEditDto {

    private String displayName;
    private String password;

    public UserInputEditDto() {
    }

    public UserInputEditDto(String displayName, String password) {
        this.displayName = displayName;
        this.password = password;
    }



    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

}
