package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.domain.Class;
import switchfully.lms.service.dto.ClassOutputDtoList;

@Component
public class ClassMapper {

    private UserMapper userMapper;
    public ClassMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Class intputToClass(ClassInputDto payload) {
        return new Class(payload.getTitle());
    }

    public ClassOutputDtoList classToOutput(Class classDomain) {
        return new ClassOutputDtoList(classDomain.getId(),
                classDomain.getTitle(),
                classDomain.getUsers().stream()
                        .map(u->userMapper.userToOutput(u)));
    }
}
