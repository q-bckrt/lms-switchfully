package switchfully.lms.service.mapper;

import org.junit.jupiter.api.Test;
import switchfully.lms.domain.*;
import switchfully.lms.domain.Class;
import switchfully.lms.domain.Module;
import switchfully.lms.service.dto.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassMapperTest {

    private final ClassMapper classMapper = new ClassMapper();

    @Test
    void givenValidClassInputDto_whenInputToClass_thenReturnClassDomain() {
        //GIVEN
        ClassInputDto input = new ClassInputDto("validTitle");
        //EXPECTED
        Class expectedResult = new Class("validTitle");

        Class result = classMapper.intputToClass(input);

        assertThat(result).isEqualTo(expectedResult);
        assertThat(result.getTitle()).isEqualTo(expectedResult.getTitle());
    }

    @Test
    void givenClassDomainEntity_whenClassToOutput_thenReturnClassDto() {
        //GIVEN
        Class classDomain = new Class("validTitle");
        Course course = new Course("validTitle");

        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto(1L,"validTitle", course.getId(),course.getTitle());

        ClassOutputDto result = classMapper.classToOutput(classDomain, course);

        assertThat(result.getTitle()).isEqualTo(expectedResult.getTitle());
    }

    @Test
    void givenClassDomainEntity_whenClassToOutputList_thenReturnClassDtoList() {
        //GIVEN
        User coach = new User("coach","coach","testFirstname","testLastName","coach@yahoo.com", UserRole.COACH);
        Course course = new Course("course");
        course.setId(4L);
        Module module = new Module("module");
        module.setId(2L);
        course.setChildModules(List.of(module));
        Class classDomain = new Class("validTitle");
        classDomain.addCoach(coach);
        classDomain.setCourse(course);
        //EXPECTED
        UserOutputDto coachDto = new UserOutputDto(coach.getUserName(),coach.getDisplayName(),coach.getEmail(), UserRole.COACH);
        ModuleOutputDto moduleDto = new ModuleOutputDto(module.getId(), "module",List.of(course.getId()), null);
        CourseOutputDto courseDto = new CourseOutputDto(course.getId(),"course",List.of(moduleDto.getId()));
        ClassOutputDtoList expectedResult = new ClassOutputDtoList(1L,courseDto,"validTitle",List.of(coachDto));

        ClassOutputDtoList result = classMapper.classToOutputList(classDomain,List.of(coachDto),courseDto);

        assertThat(result.getTitle()).isEqualTo(expectedResult.getTitle());
        assertThat(result.getCourse()).isEqualTo(expectedResult.getCourse());
        assertThat(result.getUsers()).isEqualTo(expectedResult.getUsers());
    }
}
