package switchfully.lms.service.dto;

import switchfully.lms.domain.Class;

import java.util.List;

public class UserOutputDtoList {

    private String userName;
    private String displayName;
    private List<Class> classes;

    public UserOutputDtoList() {
    }

    public UserOutputDtoList(String userName, String displayName, List<Class> classes) {
        this.userName = userName;
        this.displayName = displayName;
        this.classes = classes;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public List<Class> getClasses() {
        return classes;
    }
}
