package switchfully.lms.service.mapper;

import org.junit.jupiter.api.Test;
import switchfully.lms.service.dto.ClassInputDto;
import switchfully.lms.domain.Class;
import switchfully.lms.service.dto.ClassOutputDto;
import switchfully.lms.service.dto.ClassOutputDtoList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        //EXPECTED
        ClassOutputDto expectedResult = new ClassOutputDto("validTitle");

        ClassOutputDto result = classMapper.classToOutput(classDomain);

        assertThat(result).isEqualTo(expectedResult);
        assertThat(result.getTitle()).isEqualTo(expectedResult.getTitle());
    }
}
