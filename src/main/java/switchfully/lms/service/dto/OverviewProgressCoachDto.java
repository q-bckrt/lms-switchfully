package switchfully.lms.service.dto;

import java.util.List;

public class OverviewProgressCoachDto {
    
    private List<OverviewProgressClassDto> classes;

    public OverviewProgressCoachDto() {
    }

    public OverviewProgressCoachDto(List<OverviewProgressClassDto> classes) {
        this.classes = classes;
    }

    public List<OverviewProgressClassDto> getClasses() {
        return classes;
    }
}
