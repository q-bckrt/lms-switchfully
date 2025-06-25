package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.*;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.UserCodelabRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.*;

import static switchfully.lms.utility.validation.Validation.validateArgument;
import switchfully.lms.utility.validation.Validation;
import org.apache.commons.validator.routines.EmailValidator;
import switchfully.lms.utility.exception.InvalidInputException;
import switchfully.lms.utility.security.KeycloakService;
import switchfully.lms.utility.security.KeycloakUserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling user-related operations such as registration, profile updates,
 * and class assignments. <p>
 *
 * This service acts as a bridge between controllers and the database, coordinating user creation,
 * validation, and mapping logic. It interacts with external services (e.g., Keycloak for user management)
 * and uses various mappers and repositories to convert between domain entities and data transfer objects (DTOs). <p>
 *
 * Current functionality includes:
 * <ul>
 *   <li>Creating a new student account and saving it to the database</li>
 *   <li>Updating user profiles and synchronizing password changes with Keycloak</li>
 *   <li>Assigning users to classes and retrieving some class-related information</li>
 * </ul>
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final UserMapper userMapper;
    private final ClassMapper classMapper;
    private final KeycloakService keycloakService;
    private final CourseMapper courseMapper;
    private final UserCodelabMapper userCodelabMapper;
    private final UserCodelabRepository userCodelabRepository;
    private final CodelabRepository codelabRepository;
    private final UserCodelabService userCodelabService;
    private final ClassService classService;
    private final OverviewMapper overviewMapper;

    public UserService(UserRepository userRepository, ClassRepository classRepository,
                       UserMapper userMapper, ClassMapper classMapper, KeycloakService keycloakService,
                       CourseMapper courseMapper, UserCodelabMapper userCodelabMapper,
                       UserCodelabRepository userCodelabRepository, CodelabRepository codelabRepository,
                       UserCodelabService userCodelabService, ClassService classService,
                       OverviewMapper overviewMapper) {
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.userMapper = userMapper;
        this.classMapper = classMapper;
        this.keycloakService = keycloakService;
        this.courseMapper = courseMapper;
        this.userCodelabMapper = userCodelabMapper;
        this.userCodelabRepository = userCodelabRepository;
        this.codelabRepository = codelabRepository;
        this.userCodelabService = userCodelabService;
        this.classService = classService;
        this.overviewMapper = overviewMapper;
    }

    /** Register a new User on the database and Keycloak using a UserInputDto, the input dto contains a username, last and first name, an email and a password.
     * Some validation are performed (e.g., username not present in the database, valid email format) then user is first added to keycloak and then on the database.
     * For now, every user created using this method is a student.
     * @param userInputDto UserInputDto object
     * @see UserMapper
     * @see Validation
     * @return new UserOutputDto object
     * */
    public UserOutputDto createNewStudent(UserInputDto userInputDto) {
        UserInputDto validatedUser = validateStudentInput(userInputDto);

        User user = userMapper.inputToUser(validatedUser);
        KeycloakUserDTO keycloakUserDTO = userMapper.userInputToKeycloakUser(userInputDto);

        keycloakService.addUser(keycloakUserDTO);
        User savedUser = userRepository.save(user);

        return userMapper.userToOutput(savedUser);
    }

    /** Get the profile information for a specific user.
     * Search for the user in the database using its username.
     * Return the following information: username, display name, email and list of classes the user is part of.
     * @param username String username used to search the database,
     * @see UserRepository
     * @see UserMapper
     * @return new UserOutputDtoList object
     * */
    public UserOutputDtoList getProfile(String username) {
        User user = userRepository.findByUserName(username);
        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);
        return userMapper.userToOutputList(user, classOutputDtos);
    }

    /** Update the profile information for a specific user using a UserInputEditDto object.
     * Search for the user in the database using its username.
     * Update first the information on Keycloak (password).
     * Update second the information on the database (display name).
     * @param username String username used to search the database
     * @param userInputEditDto UserInputEditDto object
     * @see UserRepository
     * @see UserMapper
     * @return new UserOutputDtoList object
     * */
    // refactor -->  remove change password on database
    public UserOutputDtoList updateProfile(UserInputEditDto userInputEditDto, String username) {
        User user = userRepository.findByUserName(username);
        // keycloak
        KeycloakUserDTO keycloakUserDTO = userMapper.userEditToKeycloakUser(user, userInputEditDto);
        keycloakService.changePassword(keycloakUserDTO);
        // database
        user.setDisplayName(userInputEditDto.getDisplayName());
        user.setPassword(userInputEditDto.getPassword());
        User savedUser = userRepository.save(user);
        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);

        return userMapper.userToOutputList(savedUser,classOutputDtos);
    }

    /** Update the list of Class associated with a user, only one class can be added at a time. A class ID and a username must be provided.
     * Search for the user in the database using its username.
     * Check if class id is in the database.
     * Get the class from the database and add it to the list.
     * Create link between user and all the codelab related to the newly added class
     * @param username String username used to search the database
     * @param classId Long id of the class to be added to the User entity
     * @see UserRepository
     * @see ClassRepository
     * @see UserMapper
     * @see UserCodelabService
     * @return new UserOutputDtoList object
     * */
    public UserOutputDtoList updateClassInfo(Long classId, String username) {
        User user = userRepository.findByUserName(username);
        validateArgument(classId,"Class not found in repository", i->!classRepository.existsById(i),InvalidInputException::new);
        Class classDomain = classRepository.findById(classId).get();
        user.addClasses(classDomain);
        User savedUser = userRepository.save(user);

        //update link between user and codelab
        userCodelabService.updateLinkBetweenUserAndCodelabWithClassId(savedUser, classId);

        List<ClassOutputDto> classOutputDtos = getListOfClasses(user);

        return userMapper.userToOutputList(savedUser,classOutputDtos);
    }

    /** Get the overview of a class for a user.
     * Check if both user is present in the database and is a member of a class.
     * @param userName username use to retrieve a User from the database
     * @see UserRepository
     * @see Validation
     * @return ClassOutputDtoList object
     * */
    public List<ClassOutputDtoList> getClassOverview(String userName) {
        validateArgument(userName, "User not " +userName+ " found in repository",u->!userRepository.existsByUserName(u),InvalidInputException::new);
        User user = userRepository.findByUserName(userName);
        validateArgument(user.getClasses(),"This user is not part of any classes", List::isEmpty,InvalidInputException::new);

        // student only have one class
        List<Class> classesDomain = user.getClasses();

        return classesDomain.stream().map(this::GetClassDtoListUser).toList();
        //return GetClassDtoListUser(classDomain);
    }

    /** Get the progress for all the codelabs of a specific user.
     * Get user from database and the UserCodelab associated with its ID.
     * Add the title of the codelab and return a ProgressPerUserDtoList, with the username and a list of codelab title and their progress
     * @param username username use to retrieve a User from the database
     * @see UserCodelabRepository
     * @see UserCodelabMapper
     * @return ProgressPerUserDtoList object
     * */
    public ProgressPerUserDtoList getCodelabProgressPerUser(String username) {
        User user = userRepository.findByUserName(username);
        List<UserCodelab> userCodelabList = userCodelabRepository.findByIdUserId(user.getId());

        List<ProgressPerUserDto> progressDtos = userCodelabList.stream()
                .map(userCodelabMapper::userCodelabToProgressPerUserDto
                )
                .toList();

        return userCodelabMapper.usernameAndProgressPerUserDtoToProgressPerUserDtoList(username,progressDtos);

    }

    /** Validate the UserInputDto content
     * Check if username or email not already in the database and that the email has the right format.
     * @param userName user's username who wish to change his/her codelab progress
     * @param codelabId id of the codelab to be udpated
     * @param progressLevel new value for the progress
     * @throws InvalidInputException if the user/codelab pair is not present in the database
     * @return Boolean if operation went well
     * */
    public boolean updateProgressLevel(String userName, Long codelabId, String progressLevel) {
        validateArgument(userName,"User with username "+userName+" not found in database",u->!userRepository.existsByUserName(u),InvalidInputException::new);
        User user = userRepository.findByUserName(userName);
        UserCodelabId userCodelabId = new UserCodelabId(user.getId(),codelabId);
        UserCodelab userCodelab = userCodelabRepository.findById(userCodelabId).orElseThrow(() -> new InvalidInputException("This user and codelab pair does not exist."));
        userCodelab.setProgressLevel(ProgressLevel.valueOf(progressLevel.toUpperCase()));
        userCodelabRepository.save(userCodelab);

        return true;
    }

    public OverviewProgressCoachDto getOverviewCoach(String username){
        // class lie au coach
        List<Class> classes = classRepository.findClassByUserName(username);
        List<OverviewProgressClassDto> classesDto = new ArrayList<>();
        // For each class, filter the list of users to keep only students
        for (Class classDomain : classes) {
            List<OverviewProgressStudentDto> studentUsers = classDomain.getUsers().stream()
                    .filter(user -> user.getRole() == UserRole.STUDENT)
                    .map(user -> overviewMapper.userToOverviewProgressStudentDto(user, classDomain.getId()))
                    .collect(Collectors.toList());
            OverviewProgressClassDto overviewProgressClassDto = overviewMapper.classToOverviewProgressClassDto(classDomain,studentUsers);
            classesDto.add(overviewProgressClassDto);
        }

        return overviewMapper.classesToOverviewProgressCoachDto(classesDto);
    }

    /** Validate the UserInputDto content
     * Check if username or email not already in the database and that the email has the right format.
     * @param userInputDto UserInputDto to be validated
     * @throws IllegalArgumentException if criteria not met
     * @see Validation
     * @return validated UserInputDto object
     * */
    private UserInputDto validateStudentInput(UserInputDto userInputDto) {
        validateArgument(userInputDto.getEmail(), "Email already exists in the repository", userRepository::existsByEmail, InvalidInputException::new);
        validateArgument(userInputDto.getUserName(), "Username already exists in the repository", userRepository::existsByUserName, InvalidInputException::new);
        validateArgument(userInputDto.getEmail(),"Invalid email format", e-> !EmailValidator.getInstance().isValid(e),InvalidInputException::new);

        return userInputDto;
    }

    /** Get the list of classes associated with a user.
     * Classes returned only contains the title and id of the class.
     * @param user User from which we want to retrieve the classes
     * @see ClassMapper
     * @return List of Class
     * */
    private List<ClassOutputDto> getListOfClasses(User user) {
        return user.getClasses()
                .stream()
                .map(classDomain -> classMapper.classToOutput(classDomain, classDomain.getCourse()))
                .toList();
    }

    /** Private helper method that gets additional fields used in class mapper method for ClassOutputDtoList, also handles nullpointers
     * @param classDomain The {@code Class}  domain entity to map
     * @see Class
     * @see User
     * @see Course
     * @see ClassMapper
     * @return ClassOutputDtoList object
     * */
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
