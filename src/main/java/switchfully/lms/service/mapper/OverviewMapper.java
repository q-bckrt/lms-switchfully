package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.service.ClassService;
import switchfully.lms.service.dto.OverviewProgressClassDto;
import switchfully.lms.service.dto.OverviewProgressCoachDto;
import switchfully.lms.service.dto.OverviewProgressStudentDto;

import java.util.List;

@Component
public class OverviewMapper {

    private ClassService classService;

    public OverviewMapper(ClassService classService) {
        this.classService = classService;
    }

    public OverviewProgressStudentDto userToOverviewProgressStudentDto(User user, Long classId){
        return new OverviewProgressStudentDto(
                user.getUserName(),
                user.getDisplayName(),
                classService.getPercentageClassDoneForStudent(classId, user.getUserName())
        );
    }

    public OverviewProgressClassDto classToOverviewProgressClassDto(Class classDomain, List<OverviewProgressStudentDto> students) {
        return new OverviewProgressClassDto(
                classDomain.getTitle(),
                students
        );
    }

    public OverviewProgressCoachDto classesToOverviewProgressCoachDto(List<OverviewProgressClassDto> classesDto) {
        return new OverviewProgressCoachDto(
                classesDto
        );
    }
}
