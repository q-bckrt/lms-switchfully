package switchfully.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.Module;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import switchfully.lms.domain.Submodule;
import switchfully.lms.repository.CourseRepository;
import switchfully.lms.repository.ModuleRepository;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.ModuleInputDto;
import switchfully.lms.service.dto.ModuleOutputDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
 class ModuleServiceTest {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;

    private List<Submodule> childSubmodules = new ArrayList<>();
    private List<Course> parentCourses = new ArrayList<>();

    @BeforeEach
    void setUp() {
        moduleRepository.deleteAll();
        courseRepository.deleteAll();
        submoduleRepository.deleteAll();
        moduleRepository.flush();
        courseRepository.flush();
        submoduleRepository.flush();
    }

    @Test
    void givenModule_whenSave_thenCreatesNewModule() {
        //given
        ModuleInputDto moduleInputDto = new ModuleInputDto("test");
        //Expected
        Module expectedModule = new Module("test");
        // create module
        ModuleOutputDto resultModule = moduleService.createModule(moduleInputDto);
        //saved module
        Module savedModule = moduleRepository.findById(resultModule.getId()).get();
        assertThat(savedModule.getTitle()).isEqualTo(expectedModule.getTitle());
    }

    @Transactional
    @Test
    void getModuleById_retrievesModule() {
        Module module = new Module("test");
        moduleRepository.save(module);
        ModuleOutputDto savedModule = moduleService.getModuleById(module.getId());
        assertThat(savedModule).isNotNull();
        assertThat(savedModule.getTitle()).isEqualTo(module.getTitle());

    }

    @Test
    void getModuleById_throwsException_whenModuleNotFound() {
        assertThrows(Exception.class, () -> moduleService.getModuleById(50L));
    }

    @Transactional
    @Test
    void getAllModules_retrievesAllModules() {
        Module module = new Module("test");
        moduleRepository.save(module);
        Module module2 = new Module("test2");
        moduleRepository.save(module2);

        List<ModuleOutputDto> allModules = moduleService.getAllModules();
        assertThat(allModules.size()).isEqualTo(2);
        assertThat(allModules.get(0).getTitle()).isEqualTo(module.getTitle());
        assertThat(allModules.get(1).getTitle()).isEqualTo(module2.getTitle());
    }

    @Test
    @Transactional
    void updateModule_updatesModule() {
        // existing module
        Module module = new Module("test");
        Module savedModule = moduleRepository.save(module);
        //input for update
        ModuleInputDto moduleInputDto = new ModuleInputDto("testChanged");
        //when
        ModuleOutputDto moduleOutputDto = moduleService.updateModule(savedModule.getId(), moduleInputDto);
        //then
        assertThat(moduleOutputDto.getTitle()).isEqualTo(moduleInputDto.getTitle());
    }

    @Test
    void updateModule_throwsException_whenModuleNotFound() {
        //input for update
        ModuleInputDto moduleInputDto = new ModuleInputDto("testChanged");
        assertThrows(Exception.class, () -> moduleService.updateModule(50L, moduleInputDto));
    }

    @Transactional
    @Test
    void addSubmoduleToModule_returnsModuleWithAddedSubmodule() {
        //given
        Module module = new Module("test");
        Module savedModule = moduleRepository.save(module);
        Submodule submodule = new Submodule("test");
        Submodule savedSubmodule = submoduleRepository.save(submodule);
        //when
        ModuleOutputDto moduleOutputDto = moduleService.addSubmoduleToModule(savedModule.getId(), submodule.getId());
        //then
        assertThat(moduleOutputDto.getTitle()).isEqualTo(module.getTitle());
        assertThat(savedModule.getChildSubmodules().size()).isEqualTo(1);
        assertThat(savedModule.getChildSubmodules().get(0).getTitle()).isEqualTo(submodule.getTitle());
    }

    @Test
    void addSubmoduleToModule_throwsException_whenSubmoduleNotFound() {
        Module module = new Module("test");
        Module savedModule = moduleRepository.save(module);

        assertThrows(Exception.class, () -> moduleService.addSubmoduleToModule(savedModule.getId(), 50L));
    }

    @Test
    void addSubmoduleToModule_throwsException_whenModuleNotFound(){
        Submodule submodule = new Submodule("test");
        Submodule savedSubmodule = submoduleRepository.save(submodule);
        assertThrows(Exception.class, () -> moduleService.addSubmoduleToModule(50L, savedSubmodule.getId()));
    }

}
