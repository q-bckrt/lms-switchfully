package switchfully.lms.service.dto;

import java.util.List;

public class OverviewProgressClassDto {
    private String title;
    private List<OverviewProgressStudentDto> students;

    public OverviewProgressClassDto() {
    }

    public OverviewProgressClassDto(String title, List<OverviewProgressStudentDto> students) {
        this.title = title;
        this.students = students;
    }

    public String getTitle() {
        return title;
    }

    public List<OverviewProgressStudentDto> getStudents() {
        return students;
    }
}
