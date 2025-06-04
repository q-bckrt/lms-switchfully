package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.service.dto.*;
import switchfully.lms.domain.Class;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassMapper {

    public Class intputToClass(ClassInputDto payload) {
        return new Class(payload.getTitle());
    }

    public ClassOutputDtoList classToOutputList(Class classDomain, List<UserOutputDto> userList, CourseOutputDto course) {
        return new ClassOutputDtoList(classDomain.getId(),
                course,
                classDomain.getTitle(),
                userList);
    }

    public ClassOutputDto classToOutput(Class classDomain) {
        return new ClassOutputDto(classDomain.getId(),
                classDomain.getTitle()
        );
    }
}
