package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Module;
import switchfully.lms.domain.UserCodelab;

import java.util.List;

/**
 * Repository interface for managing Module entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Module
 * @see JpaRepository
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    /** Get the list of usercodelab related to a specific module and user.
     * @see switchfully.lms.service.ModuleService
     * */
    @Query(value= """
    SELECT uc.*
    FROM modules m
    JOIN modules_submodules ms ON m.id = ms.module_id
    JOIN submodules sm ON ms.submodule_id = sm.id
    JOIN codelabs cdl ON sm.id = cdl.submodule_id
    JOIN users_codelabs uc on cdl.id = uc.codelab_id
    WHERE m.id = :moduleId AND uc.user_id = :userId
    """, nativeQuery = true)
    List<UserCodelab> findProgressByModuleIdAndUserID(@Param("moduleId") Long moduleId, @Param("userId") Long userId);
}
