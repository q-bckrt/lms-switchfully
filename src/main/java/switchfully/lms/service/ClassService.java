package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.User;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.UserRole;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.CourseMapper;
import switchfully.lms.service.mapper.UserMapper;
import switchfully.lms.utility.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static switchfully.lms.utility.validation.Validation.validateArgument;
import static switchfully.lms.utility.validation.Validation.validateNonBlank;
/**
 * Service layer for handling all operations related to {@link Class} entities.
 * <p>
 * This service is responsible for:
 * <ul>
 *     <li>Validating and creating new class entities</li>
 *     <li>Fetching class details (by ID or all)</li>
 *     <li>Linking a {@link Course} to a class</li>
 * </ul>
 * <p>
 * Validation includes checking business rules such as role-based permissions
 * and unique constraints on class titles. It also ensures data consistency
 * before persisting or retrieving entities via repositories.
 * <p>
 * Exceptions thrown include:
 * <ul>
 *     <li>{@link switchfully.lms.utility.exception.InvalidInputException} for invalid input values</li>
 *     <li>{@link IllegalArgumentException} for role-based violations</li>
 * </ul>
 *
 * @see ClassRepository for database access
 * @see ClassMapper for DTO conversion
 * @see Course for linking operations
 * @see User for authorization and ownership
 */
@Service
public class ClassService {
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final ClassMapper classMapper;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;

    public ClassService(ClassRepository classRepository,
                        CourseRepository courseRepository,
                        ClassMapper classMapper, UserMapper userMapper,
                        CourseMapper courseMapper, UserRepository userRepository) {
        this.classRepository = classRepository;
        this.courseRepository = courseRepository;
        this.classMapper = classMapper;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
    }

    /** validates arguments, creates a class domain entity, persists it to the database, and maps and outputs a ClassOutputDtoList
     * @param classInputDto the input dto = json payload with information needed to create a class entity
     * @param userNameCoach the username of the coach that is creating a class, is added to user list in class upon creation
     * @see Class
     * @see ClassOutputDtoList
     * @see ClassRepository
     * @return ClassOutputDtoList object
     * */
    public ClassOutputDtoList createClass(ClassInputDto classInputDto, String userNameCoach) {
        validateArgument(userNameCoach, "User " +userNameCoach+ " not found in repository",u->!userRepository.existsByUserName(u),InvalidInputException::new);
        User coach = userRepository.findByUserName(userNameCoach);
        Class classDomain = classMapper.intputToClass(validateClassInputDto(classInputDto, coach));
        classDomain.addCoach(coach);
        classRepository.save(classDomain);

        return GetClassDtoList(classDomain);
    }

    /** Finds a specific class in the database by its id and returns a ClassOutputDto or throws an InvalidInputException
     * @param classId the {@code Long} id to search for in the class repository
     * @throws InvalidInputException if {@code classId} is not found in the repository
     * @see Class
     * @see ClassOutputDto
     * @see ClassRepository
     * @see ClassMapper
     * @return ClassOutputDto object
     * */
    public ClassOutputDto findClassById(Long classId) {
        Class classDomain = classRepository.findById(classId).orElseThrow(() -> new InvalidInputException("Class id not found in repository"));
        return classMapper.classToOutput(classDomain);
    }

    /** Finds all classes in the database and returns a list of ClassOutputDto
     * @see Class
     * @see ClassOutputDto
     * @see ClassRepository
     * @see ClassMapper
     * @return list of ClassOutputDto
     * */
    public List<ClassOutputDto> findAllClasses() {
        return classRepository.findAll().stream()
                .map(classMapper::classToOutput)
                .collect(Collectors.toList());
    }

    /** links a course to a class by setting the course field in a given class entity by providing the ids of the class and course that should be linked
     * @param classId the {@code Long} id of the class that should be linked
     * @param courseId the {@code Long} id of the course that should be linked
     * @throws InvalidInputException if either {@code classId}, or {@code courseId} are not found in their repositories
     * @see Class
     * @see Course
     * @see ClassOutputDto
     * @see ClassRepository
     * @see CourseRepository
     * @see ClassMapper
     * @return ClassOutputDto object that was linked
     * */
    public ClassOutputDto linkCourseToClass(Long classId, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new InvalidInputException("Course id not found in repository"));
        Class classDomain = classRepository.findById(classId).orElseThrow(() -> new InvalidInputException("Class id not found in repository"));
        classDomain.setCourse(course);

        return classMapper.classToOutput(classRepository.save(classDomain));
    }

    /** Private helper method that validates fields on the ClassInputDto used in class creation and returns the input dto or throws exceptions, also validates if a given user's role is permitted to create classes
     * @param classInputDto The {@code ClassInputDto} to validate fields on
     * @param user the {@code User} to validate role on
     * @throws InvalidInputException if either class title is blank, or if a class with the input title already exists in the database
     * @throws IllegalArgumentException if the user is not permitted to create classes
     * @see ClassInputDto
     * @see User
     * @return valid ClassInputDto
     * */
    private ClassInputDto validateClassInputDto(ClassInputDto classInputDto, User user) {
        //we dont have an invalidheaderexception for this first validation... is now illegalargument
        validateArgument(user.getRole(),"User "+user+" has role "+user.getRole()+" and cannot create classes",role->!role.equals(UserRole.COACH));
        validateNonBlank(classInputDto.getTitle(),"Class title cannot be blank",InvalidInputException::new);
        validateArgument(classInputDto.getTitle(),"Class title must be unique, "+classInputDto.getTitle()+" already exists", classRepository::existsByTitle, InvalidInputException::new);

        return classInputDto;
    }

    /** Private helper method that gets additional fields used in class mapper method for ClassOutputDtoList, also handles nullpointers
     * @param classDomain The {@code Class}  domain entity to map
     * @see Class
     * @see User
     * @see Course
     * @see ClassMapper
     * @return ClassOutputDtoList object
     * */
    private ClassOutputDtoList GetClassDtoList(Class classDomain) {
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
