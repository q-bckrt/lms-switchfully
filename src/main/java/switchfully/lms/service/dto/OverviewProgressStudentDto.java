package switchfully.lms.service.dto;

public class OverviewProgressStudentDto {
    //list class, list user, progresspercentage

    private String username;
    private String displayName;
    private double percentageDone;

    public OverviewProgressStudentDto() {
    }

    public OverviewProgressStudentDto(String username, String displayName, double percentageDone) {
        this.username = username;
        this.displayName = displayName;
        this.percentageDone = percentageDone;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPercentageDone() {
        return percentageDone;
    }
}
