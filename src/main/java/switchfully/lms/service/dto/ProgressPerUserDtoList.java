package switchfully.lms.service.dto;

import switchfully.lms.domain.ProgressLevel;

import java.util.List;

public class ProgressPerUserDtoList {

    private String username;
    private List<ProgressPerUserDto> progressPerUserDtoList;

    public ProgressPerUserDtoList() {
    }

    public ProgressPerUserDtoList(String username, List<ProgressPerUserDto> progressPerUserDtoList) {
        this.username = username;
        this.progressPerUserDtoList = progressPerUserDtoList;
    }

    public String getUsername() {
        return username;
    }

    public List<ProgressPerUserDto> getProgressPerUserDtoList() {
        return progressPerUserDtoList;
    }

    @Override
    public String toString() {
        return "ProgressPerUserDtoList{" +
                "username='" + username + '\'' +
                ", progressPerUserDtoList=" + progressPerUserDtoList +
                '}';
    }
}
