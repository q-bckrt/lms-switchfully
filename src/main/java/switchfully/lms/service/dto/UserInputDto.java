package switchfully.lms.service.dto;


public class UserInputDto {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserInputDto() {
    }

    public UserInputDto(String userName, String firstName, String lastName, String email, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
