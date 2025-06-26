package switchfully.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Module;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.SubmoduleInputDto;
import switchfully.lms.service.dto.SubmoduleOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SubmoduleServiceTest {

    @Autowired
    private SubmoduleRepository submoduleRepository;
    @Autowired
    private SubmoduleService submoduleService;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private CodelabRepository codelabRepository;

    private List<Module> parentModules = new ArrayList<>();
    private List<Codelab> childCodelabs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        submoduleRepository.deleteAll();
        moduleRepository.deleteAll();
        codelabRepository.deleteAll();
        submoduleRepository.flush();
        moduleRepository.flush();
        codelabRepository.flush();
    }

    @Test
    void givenSubmodule_whenSave_thenCreatesNewSubmodule() {
        //given
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("test");
        //expected
        Submodule expectedSubmodule = new Submodule("test");
        //when
        SubmoduleOutputDto resultSubmodule = submoduleService.createSubmodule(submoduleInputDto);
        //saved submodule
        Submodule actualSubmodule = submoduleRepository.findById(resultSubmodule.getId()).get();
        assertThat(actualSubmodule.getTitle()).isEqualTo(expectedSubmodule.getTitle());
    }

    @Test
    @Transactional
    void getSubmoduleById_whenSubmoduleExists_thenReturnsSubmodule() {
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);
        SubmoduleOutputDto resultSubmodule = submoduleService.getSubmoduleById(submodule.getId());

        assertThat(resultSubmodule).isNotNull();
        assertThat(resultSubmodule.getTitle()).isEqualTo(submodule.getTitle());
    }

    @Test
    void getSubmoduleById_whenSubmoduleDoesNotExist_thenThrowsException() {
        assertThrows(Exception.class, () -> submoduleService.getSubmoduleById(50L));
    }

    @Test
    @Transactional
    void getAllSubmodules_whenSubmoduleExists_thenReturnsSubmodules() {
        Submodule submodule = new Submodule("test");
        submoduleRepository.save(submodule);
        Submodule submodule2 = new Submodule("test2");
        submoduleRepository.save(submodule2);

        List<SubmoduleOutputDto> allSubmodules = submoduleService.getAllSubmodules();
        assertThat(allSubmodules.size()).isEqualTo(2);
        assertThat(allSubmodules.get(0).getTitle()).isEqualTo(submodule.getTitle());
        assertThat(allSubmodules.get(1).getTitle()).isEqualTo(submodule2.getTitle());
    }

    @Test
    @Transactional
    void updateSubmodule_whenSubmoduleExists_thenUpdatesSubmodule() {
        Submodule submodule = new Submodule("test");
        Submodule savedSubmodule = submoduleRepository.save(submodule);
        // input update
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("new_test");
        //when
        SubmoduleOutputDto resultSubmodule = submoduleService.updateSubmodule(savedSubmodule.getId(), submoduleInputDto);
        //then
        assertThat(resultSubmodule).isNotNull();
        assertThat(resultSubmodule.getTitle()).isEqualTo(submoduleInputDto.getTitle());
    }

    @Test
    void updateSubmodule_whenSubmoduleDoesNotExist_thenThrowsException() {
        SubmoduleInputDto submoduleInputDto = new SubmoduleInputDto("new_test");
        assertThrows(Exception.class, () -> submoduleService.updateSubmodule(50L, submoduleInputDto));
    }
}


