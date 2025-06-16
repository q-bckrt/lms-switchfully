package switchfully.lms.service.dto;

import switchfully.lms.domain.ProgressLevel;

public class ProgressPerUserDto {

    private String codelabTitle;
    private ProgressLevel progressLevel;

    public ProgressPerUserDto() {
    }

    public ProgressPerUserDto(String codelabTitle, ProgressLevel progressLevel) {
        this.codelabTitle = codelabTitle;
        this.progressLevel = progressLevel;
    }

    public String getCodelabTitle() {
        return codelabTitle;
    }

    public ProgressLevel getProgressLevel() {
        return progressLevel;
    }
}

