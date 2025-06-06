package switchfully.lms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CodelabMapperTest {

    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private CodelabMapper codelabMapper;
    private List<Submodule> parentSubmodules = new ArrayList<>();


    @BeforeEach
    public void cleanUp() {
        codelabRepository.deleteAll();
        codelabRepository.flush();
        submoduleRepository.deleteAll();
        submoduleRepository.flush();
        parentSubmodules.clear();
    }

    @Test
    void givenValidCodelabInputDto_whenInputDtoToCodelab_thenReturnCodelab() {
        // given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details of the codelab", parentSubmodule.getId());

        // when
        Codelab resultCodelab = codelabMapper.inputDtoToCodelab(codelabInputDto);

        // then
        assertThat(resultCodelab.getTitle()).isEqualTo(codelabInputDto.getTitle());
        assertThat(resultCodelab.getDetails()).isEqualTo(codelabInputDto.getDetails());
        assertThat(resultCodelab.getParentSubmodule().getId()).isEqualTo(codelabInputDto.getParentSubmoduleId());
    }

    @Test
    void givenValidCodelab_whenModuleToOutputDto_thenReturnOutputDto() {
        // given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        Codelab codelab = new Codelab("Test Codelab", "Details of the codelab", parentSubmodule);
        Codelab savedCodelab = codelabRepository.save(codelab);

        // when
        CodelabOutputDto resultDto = codelabMapper.codelabToOutputDto(savedCodelab);

        // then
        assertThat(resultDto.getId()).isEqualTo(savedCodelab.getId());
        assertThat(resultDto.getTitle()).isEqualTo(savedCodelab.getTitle());
        assertThat(resultDto.getDetails()).isEqualTo(savedCodelab.getDetails());
        assertThat(resultDto.getParentSubmoduleId()).isEqualTo(savedCodelab.getParentSubmodule().getId());
    }
}
