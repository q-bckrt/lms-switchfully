package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;
import switchfully.lms.service.dto.UserOutputDto;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;
import switchfully.lms.utility.exception.InvalidInputException;

import java.util.List;
import java.util.stream.Collectors;

import static switchfully.lms.utility.validation.Validation.validateArgument;
import static switchfully.lms.utility.validation.Validation.validateNonBlank;

@Service
public class ClassService {
    private final ClassRepository classRepository;
    private final ClassMapper classMapper;
    private final UserMapper userMapper;

    public ClassService(ClassRepository classRepository, ClassMapper classMapper, UserMapper userMapper) {
        this.classRepository = classRepository;
        this.classMapper = classMapper;
        this.userMapper = userMapper;
    }

    public ClassOutputDtoList createClass(ClassInputDto classInputDto, User coach) {
        //coach is validated to be coach in controller
        validateNonBlank(classInputDto.getTitle(),"Class title cannot be blank",InvalidInputException::new);
        Class classDomain = classMapper.intputToClass(classInputDto);
        classDomain.addCoach(coach);
        classRepository.save(classDomain);
        List<UserOutputDto> userList = classDomain.getUsers().stream()
                .map(userMapper::userToOutput)
                .collect(Collectors.toList());
        return classMapper.classToOutputList(classDomain,userList);
    }

//    public ClassOutputDtoList getClassOverview(Long classId, User user) {
//        validateArgument(user.getClasses(),"This user is not part of any classes", List::isEmpty,InvalidInputException::new);
//        return findClassById(classId);
//    }

//    public ClassOutputDtoList findClassById(Long classId) {
//        return classMapper.classToOutputList(classRepository.findById(classId).orElseThrow(() -> new InvalidInputException("Class id not found in repository")));
//    }
//
//    public List<ClassOutputDtoList> findAllClasses() {
//        return classRepository.findAll().stream()
//                .map(classMapper::classToOutputList)
//                .collect(Collectors.toList());
//    }

}
