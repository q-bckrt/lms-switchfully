package switchfully.lms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ModuleMapperTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper = new ModuleMapper(courseRepository);

    private List<Submodule> childSubmodules = new ArrayList<>();
    private List<Course> parentCourses = new ArrayList<>();

    @BeforeEach
    public void cleanUp() {
        moduleRepository.deleteAll();
        courseRepository.deleteAll();
        moduleRepository.flush();
        courseRepository.flush();
    }

    @Test
    void givenValidModuleInputDto_whenInpuDtoToModule_thenReturnModule() {
        // given
        ModuleInputDto moduleInputDto = new ModuleInputDto("test");
        //Expected
        Module expectedModule = new Module("test");
        //when
        Module resultModule = moduleMapper.inputDtoToModule(moduleInputDto);
        //then
        assertThat(resultModule).isEqualTo(expectedModule);
        assertThat(resultModule.getTitle()).isEqualTo(expectedModule.getTitle());
    }

    @Test
    void givenValidModule_when_moduleToOutputDto_thenReturnOutputDto() {
        //given
        Module module = new Module("test");
        Module savedModule = moduleRepository.save(module);
        // Expected
        ModuleOutputDto expectedOutputDto = new ModuleOutputDto(1L, "test", parentCourses.stream().map(Course::getId).toList(), childSubmodules.stream().map(Submodule::getId).toList());
        //when
        ModuleOutputDto resultOutputDto = moduleMapper.moduleToOutputDto(module);
        //then
        assertThat(resultOutputDto).isEqualTo(expectedOutputDto);
        assertThat(resultOutputDto.getTitle()).isEqualTo(expectedOutputDto.getTitle());
    }

}
