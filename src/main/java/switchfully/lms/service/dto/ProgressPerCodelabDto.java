package switchfully.lms.service.dto;

import switchfully.lms.domain.ProgressLevel;

public class ProgressPerCodelabDto {

    String userDisplayName;
    private ProgressLevel progressLevel;

    public ProgressPerCodelabDto() {
    }

    public ProgressPerCodelabDto(String userDisplayName, ProgressLevel progressLevel) {
        this.userDisplayName = userDisplayName;
        this.progressLevel = progressLevel;
    }

    public String getDisplayName() {
        return this.userDisplayName;
    }

    public ProgressLevel getProgressLevel() {
        return this.progressLevel;
    }

    @Override
    public String toString() {
        return "ProgressPerCodelabDto{" +
                "displayName='" + userDisplayName + '\'' +
                ", progressLevel=" + progressLevel +
                '}';
    }
}
