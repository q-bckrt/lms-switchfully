package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.CourseMapper;
import switchfully.lms.service.mapper.UserMapper;
import static switchfully.lms.utility.validation.Validation.validateArgument;
import org.apache.commons.validator.routines.EmailValidator;
import switchfully.lms.utility.exception.InvalidInputException;
import switchfully.lms.utility.security.KeycloakService;
import switchfully.lms.utility.security.KeycloakUserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final UserMapper userMapper;
    private final ClassMapper classMapper;
    private final KeycloakService keycloakService;
    private final CourseMapper courseMapper;

    public UserService(UserRepository userRepository, ClassRepository classRepository,
                       UserMapper userMapper, ClassMapper classMapper, KeycloakService keycloakService,
                       CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.userMapper = userMapper;
        this.classMapper = classMapper;
        this.keycloakService = keycloakService;
        this.courseMapper = courseMapper;
    }

    public UserOutputDto createNewStudent(UserInputDto userInputDto) {
        UserInputDto validatedUser = validateStudentInput(userInputDto);

        User user = userMapper.inputToUser(validatedUser);
        KeycloakUserDTO keycloakUserDTO = userMapper.userInputToKeycloakUser(userInputDto);

        keycloakService.addUser(keycloakUserDTO);
        User savedUser = userRepository.save(user);

        return userMapper.userToOutput(savedUser);
    }

    public UserOutputDtoList getProfile(String username) {
        User user = userRepository.findByUserName(username);
        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);
        return userMapper.userToOutputList(user, classOutputDtos);
    }

    // refactor --> do not change username, remove role and remove change password, add update password from keycloak
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

    public ClassOutputDtoList getClassOverview(String userName) {
        validateArgument(userName, "User not " +userName+ " found in repository",u->!userRepository.existsByUserName(u),InvalidInputException::new);
        User user = userRepository.findByUserName(userName);
        validateArgument(user.getClasses(),"This user is not part of any classes", List::isEmpty,InvalidInputException::new);

        // student only have one class
        Class classDomain = user.getClasses().get(0);

        return GetClassDtoListUser(classDomain);
    }

    private ClassOutputDtoList GetClassDtoListUser(Class classDomain) {
        List<UserOutputDto> userList = new ArrayList<>();
        if(!classDomain.getUsers().isEmpty()) {
            userList = classDomain.getUsers().stream()
                    .map(userMapper::userToOutput)
                    .collect(Collectors.toList());
        }

        CourseOutputDto courseOutputDto = null;
        if(classDomain.getCourse() != null) {
            courseOutputDto = courseMapper.courseToOutputDto(classDomain.getCourse());
        }

        return classMapper.classToOutputList(classDomain,userList,courseOutputDto);
    }
}
