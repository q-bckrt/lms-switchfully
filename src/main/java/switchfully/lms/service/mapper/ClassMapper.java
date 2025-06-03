package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.service.dto.*;
import switchfully.lms.domain.Class;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassMapper {

    private final UserMapper userMapper;
    public ClassMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Class intputToClass(ClassInputDto payload) {
        return new Class(payload.getTitle());
    }

    public ClassOutputDtoList classToOutputList(Class classDomain, List<UserOutputDto> userList) {
        return new ClassOutputDtoList(classDomain.getId(),
                classDomain.getTitle(),
                userList);
    }

    public ClassOutputDto classToOutput(Class classDomain) {
        return new ClassOutputDto(
                classDomain.getTitle()
        );
    }
}
