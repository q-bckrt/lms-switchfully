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

    /**
     * Converts a User domain object to a ProgressStudentDto to match need of frontend.
     *
     * @param user The user domain object to convert
     * @param classId id of the class the user belongs to
     * @return A DTO containing the data of the ProgressStudentDto
     */
    public OverviewProgressStudentDto userToOverviewProgressStudentDto(User user, Long classId){
        return new OverviewProgressStudentDto(
                user.getUserName(),
                user.getDisplayName(),
                classService.getPercentageClassDoneForStudent(classId, user.getUserName())
        );
    }

    /**
     * Converts a Class domain object and its list of students to a ProgressClassDto to match need of frontend.
     *
     * @param classDomain The class domain object to convert
     * @param students list of students of the class, already converted to ProgressStudentDto
     * @return A DTO containing the data of the ProgressClassDto
     */
    public OverviewProgressClassDto classToOverviewProgressClassDto(Class classDomain, List<OverviewProgressStudentDto> students) {
        return new OverviewProgressClassDto(
                classDomain.getTitle(),
                students
        );
    }

    /**
     * Converts a list of class domain object to OverviewProgressCoachDto to match need of frontend.
     *
     * @param classesDto list of classes
     * @return A DTO containing the data of the OverviewProgressCoachDto
     */
    public OverviewProgressCoachDto classesToOverviewProgressCoachDto(List<OverviewProgressClassDto> classesDto) {
        return new OverviewProgressCoachDto(
                classesDto
        );
    }
}
