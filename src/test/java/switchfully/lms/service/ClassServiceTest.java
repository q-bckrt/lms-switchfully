package switchfully.lms.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import static org.mockito.ArgumentMatchers.*;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserRole;
import switchfully.lms.domain.Class;
import switchfully.lms.repository.ClassRepository;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.ClassMapper;
import switchfully.lms.service.mapper.CourseMapper;
import switchfully.lms.service.mapper.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClassServiceTest {
    @InjectMocks
    private ClassService classService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ClassMapper classMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CourseMapper courseMapper;

    User student1, student2, student3;
    UserOutputDto student1Dto, student2Dto, student3Dto;
    User coach;
    UserOutputDto coachDto;

    Course courseJava;
    CourseOutputDto courseJavaDto;

    Class classEnrolled;
    ClassOutputDtoList classOutputDtoList;
    ClassOutputDto classOutputDto;

    public static void setId(Object target, Long idValue) {
        try {
            Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, idValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field id via reflection", e);
        }
    }

//    @BeforeAll
//    public void beforeAll() {
//
//        student1 = new User("ann_DM","Ann Demeulemeester","ann@yahoo.com","pass", UserRole.STUDENT);
//        student2 = new User("jil_sander","Jil Sander","jil@yahoo.com","pass", UserRole.STUDENT);
//        student3 = new User("maarten_mar","Maarten Margiela","maarten@yahoo.com","pass", UserRole.STUDENT);
//        coach = new User("elsa_schiap","Elsa Schiaparelli","elsa@yahoo.com","pass", UserRole.COACH);
//
//        setId(student1, 1L);
//        setId(student2, 2L);
//        setId(student3, 3L);
//        setId(coach, 4L);
//    }

    @BeforeEach
    public void beforeEach() {

        student1 = new User("ann_DM","Ann Demeulemeester","testFirstname","testLastName","ann@yahoo.com","pass", UserRole.STUDENT);
        student2 = new User("jil_sander","Jil Sander","testFirstname","testLastName","jil@yahoo.com","pass", UserRole.STUDENT);
        student3 = new User("maarten_mar","Maarten Margiela","testFirstname","testLastName","maarten@yahoo.com","pass", UserRole.STUDENT);
        coach = new User("elsa_schiap","Elsa Schiaparelli","testFirstname","testLastName","elsa@yahoo.com","pass", UserRole.COACH);

        setId(student1, 1L);
        setId(student2, 2L);
        setId(student3, 3L);
        setId(coach, 4L);

        student1.setClasses(new ArrayList<>());
        student2.setClasses(new ArrayList<>());
        student3.setClasses(new ArrayList<>());
        coach.setClasses(new ArrayList<>());

        courseJava = new Course("JAVA FUNDAMENTALS");
        classEnrolled = new Class("JAVA_2025");

        setId(courseJava, 1L);
        setId(classEnrolled, 1L);

        classEnrolled.addCoach(coach);
        classEnrolled.setCourse(courseJava);
        coach.addClasses(classEnrolled);
        student1.addClasses(classEnrolled);
        student2.addClasses(classEnrolled);
        student3.addClasses(classEnrolled);

        lenient().when(userRepository.findByUserName(student1.getUserName())).thenReturn(student1);
        lenient().when(userRepository.findByUserName(student2.getUserName())).thenReturn(student2);
        lenient().when(userRepository.findByUserName(student3.getUserName())).thenReturn(student3);
        lenient().when(userRepository.findByUserName(coach.getUserName())).thenReturn(coach);
        lenient().when(userRepository.findAll()).thenReturn(List.of(student1, student2, student3, coach));

        lenient().when(userRepository.existsByUserName(student1.getUserName())).thenReturn(true);
        lenient().when(userRepository.existsByUserName(student2.getUserName())).thenReturn(true);
        lenient().when(userRepository.existsByUserName(student3.getUserName())).thenReturn(true);
        lenient().when(userRepository.existsByUserName(coach.getUserName())).thenReturn(true);

        student1Dto = new UserOutputDto(student1.getUserName(),student1.getDisplayName(),student1.getEmail());
        student2Dto = new UserOutputDto(student2.getUserName(),student2.getDisplayName(),student2.getEmail());
        student3Dto = new UserOutputDto(student3.getUserName(),student3.getDisplayName(),student3.getEmail());
        coachDto = new UserOutputDto(coach.getUserName(),coach.getDisplayName(),coach.getEmail());

        lenient().when(userMapper.userToOutput(student1)).thenReturn(student1Dto);
        lenient().when(userMapper.userToOutput(student2)).thenReturn(student2Dto);
        lenient().when(userMapper.userToOutput(student3)).thenReturn(student3Dto);
        lenient().when(userMapper.userToOutput(coach)).thenReturn(coachDto);

        courseJavaDto = new CourseOutputDto(courseJava.getId(), courseJava.getTitle(), null);
        lenient().when(courseMapper.courseToOutputDto(courseJava)).thenReturn(courseJavaDto);

        List<UserOutputDto> userDtoList = new ArrayList<>();
        userDtoList.add(coachDto);
        userDtoList.add(student1Dto);
        userDtoList.add(student2Dto);
        userDtoList.add(student3Dto);

        classOutputDtoList = new ClassOutputDtoList(classEnrolled.getId(),courseJavaDto, classEnrolled.getTitle(), userDtoList);
        lenient().when(classMapper.classToOutputList(eq(classEnrolled),anyList(),eq(courseJavaDto))).thenReturn(classOutputDtoList);

        classOutputDto = new ClassOutputDto(classEnrolled.getId(),classEnrolled.getTitle());
        lenient().when(classMapper.classToOutput(classEnrolled)).thenReturn(classOutputDto);

        lenient().when(classRepository.findById(classEnrolled.getId())).thenReturn(Optional.of(classEnrolled));
        lenient().when(courseRepository.findById(courseJava.getId())).thenReturn(Optional.of(courseJava));
    }

    @Test
    void givenCoachAuthenticatedAndInputValid_whenCreateClass_thenReturnClassDtoListWith() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("NEW CLASS");
        Class classDomain = new Class("NEW CLASS");
        classDomain.addCoach(coach);
        setId(classDomain, 2L);

        UserOutputDto coachDto = userMapper.userToOutput(coach);

        when(classMapper.intputToClass(classInputDto)).thenReturn(classDomain);
        when(classMapper.classToOutputList(eq(classDomain), anyList(), isNull()))
                .thenReturn(new ClassOutputDtoList(2L, null, "NEW CLASS", List.of(coachDto)));

        //EXPECTED RESULT
        ClassOutputDtoList classOutputDto = new ClassOutputDtoList(classDomain.getId(),null,"NEW CLASS",List.of(coachDto));
        //WHEN THEN
        ClassOutputDtoList result = classService.createClass(classInputDto,coach.getUserName());

        assertThat(result.getTitle()).isEqualTo(classOutputDto.getTitle());
        assertThat(result.getUsers()).isEqualTo(classOutputDto.getUsers());
    }

    @Test
    void givenStudentAuthenticated_whenCreateClass_thenThrowsException() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("NEW CLASS");

        assertThrows(IllegalArgumentException.class, () -> classService.createClass(classInputDto,student2.getUserName()));
    }

    @Test
    void givenCoachAuthenticatedAndInputInValid_whenCreateClass_thenReturnClassDtoListWith() {
        //GIVEN
        ClassInputDto classInputDto = new ClassInputDto("   ");

        assertThrows(IllegalArgumentException.class, () -> classService.createClass(classInputDto,student2.getUserName()));
    }

//    @Test
//    void givenClassWithStudentsExists_whenGetClassOverview_thenReturnClassOverview() {
//        ClassOutputDtoList result = classService.getClassOverview(classEnrolled.getId(),coach.getUserName());
//
//        assertThat(result).isEqualTo(classOutputDtoList);
//    }

    @Test
    void givenClassExistsInRepo_whenFindClassById_thenReturnClassDto() {
        ClassOutputDto result = classService.findClassById(classEnrolled.getId());

        assertThat(result).isEqualTo(classOutputDto);
    }

    @Test
    void givenMultipleClassesExistInRepo_whenFindAllClasses_thenReturnListOfClassDto() {
        Class classDomain1 = new Class("NEW CLASS1");
        Class classDomain2 = new Class("NEW CLASS2");
        Class classDomain3 = new Class("NEW CLASS3");
        classDomain1.addCoach(coach);
        classDomain2.addCoach(coach);
        classDomain3.addCoach(coach);
        setId(classDomain1, 90L);
        setId(classDomain2, 91L);
        setId(classDomain3, 92L);

        ClassOutputDto classDomain1Dto = new ClassOutputDto(classDomain1.getId(),classDomain1.getTitle());
        ClassOutputDto classDomain2Dto = new ClassOutputDto(classDomain2.getId(),classDomain2.getTitle());
        ClassOutputDto classDomain3Dto = new ClassOutputDto(classDomain3.getId(),classDomain3.getTitle());


        when(classRepository.findAll()).thenReturn(List.of(classDomain1, classDomain2, classDomain3));
        when(classMapper.classToOutput(classDomain1)).thenReturn(classDomain1Dto);
        when(classMapper.classToOutput(classDomain2)).thenReturn(classDomain2Dto);
        when(classMapper.classToOutput(classDomain3)).thenReturn(classDomain3Dto);

        //EXPECTED RESULT
        List<ClassOutputDto> expectedResult = new ArrayList<>();
        expectedResult.add(classDomain1Dto);
        expectedResult.add(classDomain2Dto);
        expectedResult.add(classDomain3Dto);
        //RESULT
        List<ClassOutputDto> result = classService.findAllClasses();

        assertThat(result).isEqualTo(expectedResult);
        assertThat(result.size()).isEqualTo(expectedResult.size());
    }

    @Test
    void givenCourseAndClassExistInRepos_whenLinkCourseToClass_thenReturnClassOutputDto() {
        Class classDomain = new Class("NEW CLASS");
        Course course = new Course("NEW COURSE");
        setId(classDomain, 20L);
        setId(course, 67L);
        classDomain.addCoach(coach);
        classDomain.setCourse(course);

        ClassOutputDto classDomainDto = new ClassOutputDto(classDomain.getId(),classDomain.getTitle());

        when(classRepository.findById(classDomain.getId())).thenReturn(Optional.of(classDomain));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(classRepository.save(classDomain)).thenReturn(classDomain);
        when(classMapper.classToOutput(classDomain)).thenReturn(classDomainDto);

        ClassOutputDto result = classService.linkCourseToClass(classDomain.getId(),course.getId());

        assertThat(result).isEqualTo(classDomainDto);
    }
}
