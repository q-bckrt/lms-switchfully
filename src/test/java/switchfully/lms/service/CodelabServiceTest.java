package switchfully.lms.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Codelab;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CodelabServiceTest {

    @Autowired
    private CodelabService codelabService;
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;

    @BeforeEach
    void setUp() {
        codelabRepository.deleteAll();
        submoduleRepository.deleteAll();
        codelabRepository.flush();
        submoduleRepository.flush();
    }

    @Transactional
    @Test
    void givenCodelab_whenSave_thenCreatesNewCodelab() {
        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details about the codelab", parentSubmodule.getId());

        //when
        CodelabOutputDto savedCodelab = codelabService.createCodelab(codelabInputDto);

        //then
        assertThat(savedCodelab).isNotNull();
        assertThat(savedCodelab.getTitle()).isEqualTo("Test Codelab");
        assertThat(savedCodelab.getDetails()).isEqualTo("Details about the codelab");
        assertThat(savedCodelab.getParentSubmoduleId()).isEqualTo(parentSubmodule.getId());
    }

    @Transactional
    @Test
    void givenCodelab_whenSaveWithNullParent_thenThrowsException() {
        //given
        CodelabInputDto codelabInputDto = new CodelabInputDto("Test Codelab", "Details about the codelab", null);

        //when & then
        assertThrows(Exception.class, () -> codelabService.createCodelab(codelabInputDto));
    }

    @Transactional
    @Test
    void getCodelabById_retrievesCodelab() {
        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        Codelab codelab = new Codelab("Test Codelab", "Details about the codelab", parentSubmodule);
        codelabRepository.save(codelab);

        //when
        CodelabOutputDto savedCodelab = codelabService.getCodelabById(codelab.getId());

        //then
        assertThat(savedCodelab).isNotNull();
        assertThat(savedCodelab.getTitle()).isEqualTo(codelab.getTitle());
        assertThat(savedCodelab.getDetails()).isEqualTo(codelab.getDetails());
        assertThat(savedCodelab.getParentSubmoduleId()).isEqualTo(parentSubmodule.getId());
    }

    @Transactional
    @Test
    void getCodelabById_throwsException_whenCodelabNotFound() {
        //when & then
        assertThrows(Exception.class, () -> codelabService.getCodelabById(50L));
    }

    @Transactional
    @Test
    void getAllCodelabs_retrievesAllCodelabs() {
        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        Codelab codelab1 = new Codelab("Test Codelab 1", "Details about the codelab 1", parentSubmodule);
        Codelab codelab2 = new Codelab("Test Codelab 2", "Details about the codelab 2", parentSubmodule);
        codelabRepository.save(codelab1);
        codelabRepository.save(codelab2);

        //when
        List<CodelabOutputDto> allCodelabs = codelabService.getAllCodelabs();

        //then
        assertThat(allCodelabs.size()).isEqualTo(2);
        assertThat(allCodelabs.get(0).getTitle()).isEqualTo(codelab1.getTitle());
        assertThat(allCodelabs.get(1).getTitle()).isEqualTo(codelab2.getTitle());
    }

    @Transactional
    @Test
    void updateCodelab_updatesCodelab() {
        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        Codelab codelab = new Codelab("Test Codelab", "Details about the codelab", parentSubmodule);
        Codelab savedCodelab = codelabRepository.save(codelab);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Updated Codelab", "Updated details about the codelab", parentSubmodule.getId());

        //when
        CodelabOutputDto updatedCodelab = codelabService.updateCodelab(savedCodelab.getId(), codelabInputDto);

        //then
        assertThat(updatedCodelab.getTitle()).isEqualTo(codelabInputDto.getTitle());
        assertThat(updatedCodelab.getDetails()).isEqualTo(codelabInputDto.getDetails());
    }

    @Transactional
    @Test
    void updateCodelab_throwsException_whenCodelabNotFound() {
        //given
        Submodule parentSubmodule = new Submodule("Parent Submodule");
        submoduleRepository.save(parentSubmodule);

        CodelabInputDto codelabInputDto = new CodelabInputDto("Updated Codelab", "Updated details about the codelab", parentSubmodule.getId());

        //when & then
        assertThrows(Exception.class, () -> codelabService.updateCodelab(50L, codelabInputDto));
    }

}