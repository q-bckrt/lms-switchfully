package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.User;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.UserMapper;
import static switchfully.lms.utility.validation.Validation.validateArgument;
import org.apache.commons.validator.routines.EmailValidator;
import switchfully.lms.utility.exception.InvalidInputException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ClassMapper classMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, ClassMapper classMapper) {
        this.userRepository = userRepository;
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
        List<ClassOutputDto> classOutputDtos = user.getClasses()
                .stream()
                .map(classMapper::classToOutput)
                .collect(Collectors.toList());

        return userMapper.userToOutputList(user, classOutputDtos);
    }

    public UserOutputDtoList updateProfile(UserInputEditDto userInputEditDto, String username) {
        User user = userRepository.findByUserName(username);
        UserInputEditDto validatedUser = validateStudentInputEdit(userInputEditDto);

        user.setUserName(validatedUser.getUserName());
        user.setDisplayName(validatedUser.getDisplayName());
        user.setPassword(validatedUser.getPassword());
        User savedUser = userRepository.save(user);
        List<ClassOutputDto> classOutputDtos = user.getClasses()
                .stream()
                .map(classMapper::classToOutput)
                .collect(Collectors.toList());

        return userMapper.userToOutputList(savedUser,classOutputDtos);
    }

    private UserInputDto validateStudentInput(UserInputDto userInputDto) {
        validateArgument(userInputDto.getEmail(), "Email already exists in the repository", userRepository::existsByEmail, InvalidInputException::new);
        validateArgument(userInputDto.getUserName(), "Username already exists in the repository", userRepository::existsByUserName, InvalidInputException::new);
        validateArgument(userInputDto.getEmail(),"Invalid email format", e-> !EmailValidator.getInstance().isValid(e),InvalidInputException::new);
        if(!Objects.equals(userInputDto.getPassword(), userInputDto.getPasswordControl())){
            throw new InvalidInputException("The passwords do not match");
        }

        return userInputDto;
    }

    private UserInputEditDto validateStudentInputEdit(UserInputEditDto userInputEditDto) {
        validateArgument(userInputEditDto.getUserName(), "Username already exists in the repository", userRepository::existsByUserName, InvalidInputException::new);
        if(!Objects.equals(userInputEditDto.getPassword(), userInputEditDto.getPasswordControl())){
            throw new InvalidInputException("The passwords do not match");
        }
        return userInputEditDto;
    }
}
