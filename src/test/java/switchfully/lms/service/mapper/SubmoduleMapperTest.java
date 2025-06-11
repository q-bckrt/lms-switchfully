package switchfully.lms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Module;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SubmoduleMapperTest {

    @Autowired
    private SubmoduleRepository submoduleRepository;

    private final SubmoduleMapper submoduleMapper = new SubmoduleMapper();

    private List<Module> parentModules = new ArrayList<>();
    private List<Codelab> childCodelabs = new ArrayList<>();

    @BeforeEach
    public void setup(){
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
    }

    @Test
    void givenValidSubmoduleInputDto_whenInputDtoToSubmodule_thenReturnSubmodule() {
        //given
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("test");
        //expected
        Submodule expectedSubmodule = new Submodule("test");
        //when
        Submodule resultSubmodule = submoduleMapper.inputDtoToSubmodule(submoduleInputDto);
        //then
        assertThat(resultSubmodule).isEqualTo(expectedSubmodule);
        assertThat(resultSubmodule.getTitle()).isEqualTo(expectedSubmodule.getTitle());
    }

    @Test
    void givenValidSubmodule_when_submoduleToOutputDto_thenReturnOutputDto(){
        //given
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);
        //Expected
        SubmoduleOutputDto expectedOutputDto = new SubmoduleOutputDto(1L,"test", parentModules.stream().map(Module::getId).toList(), childCodelabs.stream().map(Codelab::getId).toList());
        //when
        SubmoduleOutputDto resultOutputDto = submoduleMapper.submoduleToOutputDto(submodule);
        //then
        assertThat(resultOutputDto).isEqualTo(expectedOutputDto);
        assertThat(resultOutputDto.getTitle()).isEqualTo(expectedOutputDto.getTitle());
    }
}
