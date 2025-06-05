package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;
import static switchfully.lms.utility.validation.Validation.validateArgument;
import org.apache.commons.validator.routines.EmailValidator;
import switchfully.lms.utility.exception.InvalidInputException;

import java.util.List;
import java.util.Objects;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final UserMapper userMapper;
    private final ClassMapper classMapper;

    public UserService(UserRepository userRepository, ClassRepository classRepository, UserMapper userMapper, ClassMapper classMapper) {
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.userMapper = userMapper;
        this.classMapper = classMapper;
    }

    public UserOutputDto createNewStudent(UserInputDto userInputDto) {
        UserInputDto validatedUser = validateStudentInput(userInputDto);
        User user = userMapper.inputToUser(validatedUser);
        User savedUser = userRepository.save(user);
        return userMapper.userToOutput(savedUser);
    }

    public UserOutputDtoList getProfile(String username) {
        User user = userRepository.findByUserName(username);
        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);
        return userMapper.userToOutputList(user, classOutputDtos);
    }

    public UserOutputDtoList updateProfile(UserInputEditDto userInputEditDto, String username) {
        User user = userRepository.findByUserName(username);

        if (!Objects.equals(username, userInputEditDto.getUserName())) {
            validateArgument(userInputEditDto.getUserName(), "Username already exists in the repository", userRepository::existsByUserName, InvalidInputException::new);
        }
        user.setUserName(userInputEditDto.getUserName());
        user.setDisplayName(userInputEditDto.getDisplayName());
        user.setPassword(userInputEditDto.getPassword());
        User savedUser = userRepository.save(user);
        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);

        return userMapper.userToOutputList(savedUser,classOutputDtos);
    }

    public UserOutputDtoList updateClassInfo(Long classId, String username) {
        User user = userRepository.findByUserName(username);
        validateArgument(classId,"Class not found in repository", i->!classRepository.existsById(i),InvalidInputException::new);
        Class classDomain = classRepository.findById(classId).get();
        user.addClasses(classDomain);
        User savedUser = userRepository.save(user);

        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);

        return userMapper.userToOutputList(savedUser,classOutputDtos);
    }

    private UserInputDto validateStudentInput(UserInputDto userInputDto) {
        validateArgument(userInputDto.getEmail(), "Email already exists in the repository", userRepository::existsByEmail, InvalidInputException::new);
        validateArgument(userInputDto.getUserName(), "Username already exists in the repository", userRepository::existsByUserName, InvalidInputException::new);
        validateArgument(userInputDto.getEmail(),"Invalid email format", e-> !EmailValidator.getInstance().isValid(e),InvalidInputException::new);

        return userInputDto;
    }

    private List<ClassOutputDto> getListOfClasses(User user) {
        List<ClassOutputDto> classOutputDtos;
        return classOutputDtos = user.getClasses()
                .stream()
                .map(classMapper::classToOutput)
                .toList();
    }
}
