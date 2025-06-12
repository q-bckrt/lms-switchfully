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

    public ClassOutputDtoList createClass(ClassInputDto classInputDto, String userNameCoach) {
        validateArgument(userNameCoach, "User " +userNameCoach+ " not found in repository",u->!userRepository.existsByUserName(u),InvalidInputException::new);
        User coach = userRepository.findByUserName(userNameCoach);
        Class classDomain = classMapper.intputToClass(validateClassInputDto(classInputDto, coach));
        classDomain.addCoach(coach);
        classRepository.save(classDomain);

        return GetClassDtoList(classDomain);
    }

    public ClassOutputDto findClassById(Long classId) {
        Class classDomain = classRepository.findById(classId).orElseThrow(() -> new InvalidInputException("Class id not found in repository"));
        return classMapper.classToOutput(classDomain);
    }

    public List<ClassOutputDto> findAllClasses() {
        return classRepository.findAll().stream()
                .map(classMapper::classToOutput)
                .collect(Collectors.toList());
    }

    public ClassOutputDto linkCourseToClass(Long classId, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new InvalidInputException("Course id not found in repository"));
        Class classDomain = classRepository.findById(classId).orElseThrow(() -> new InvalidInputException("Class id not found in repository"));
        classDomain.setCourse(course);

        return classMapper.classToOutput(classRepository.save(classDomain));
    }

    private ClassInputDto validateClassInputDto(ClassInputDto classInputDto, User user) {
        //we dont have an invalidheaderexception for this first validation... is now illegalargument
        validateArgument(user.getRole(),"User "+user+" has role "+user.getRole()+" and cannot create classes",role->!role.equals(UserRole.COACH));
        validateNonBlank(classInputDto.getTitle(),"Class title cannot be blank",InvalidInputException::new);
        validateArgument(classInputDto.getTitle(),"Class title must be unique, "+classInputDto.getTitle()+" already exists", classRepository::existsByTitle, InvalidInputException::new);

        return classInputDto;
    }

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
