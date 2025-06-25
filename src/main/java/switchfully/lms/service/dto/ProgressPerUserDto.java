package switchfully.lms.service.dto;

import switchfully.lms.domain.ProgressLevel;

public class ProgressPerUserDto {

    private String codelabTitle;
    private ProgressLevel progressLevel;
    private Long codelabId;

    public ProgressPerUserDto() {
    }

    public ProgressPerUserDto(String codelabTitle, ProgressLevel progressLevel, Long codelabId) {
        this.codelabTitle = codelabTitle;
        this.progressLevel = progressLevel;
        this.codelabId = codelabId;
    }

    public String getCodelabTitle() {
        return codelabTitle;
    }

    public ProgressLevel getProgressLevel() {
        return progressLevel;
    }

    public Long getCodelabId() {
        return codelabId;
    }

    @Override
    public String toString() {
        return "ProgressPerUserDto{" +
                "codelabTitle='" + codelabTitle + '\'' +
                ", progressLevel=" + progressLevel +
                '}';
    }
}

