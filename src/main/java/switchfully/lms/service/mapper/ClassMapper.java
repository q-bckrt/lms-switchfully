package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.domain.Class;
import switchfully.lms.service.dto.ClassOutputDto;

@Component
public class ClassMapper {

    public Class intputToClass(ClassInputDto payload) {
        return new Class(payload.getTitle());
    }

    public ClassOutputDto classToOutput(Class classDomain) {
        return new ClassOutputDto(classDomain.getId(),
                classDomain.getTitle(),
                classDomain.getUsers());
    }
}
