package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.domain.UserCodelabId;
import switchfully.lms.domain.UserRole;

import java.util.List;

public interface UserCodelabRepository extends JpaRepository<UserCodelab, UserCodelabId> {

    List<UserCodelab> findByIdUserId(Long userId);

    Boolean existsByUserIdAndCodelabId(Long userId, Long codelabId);

    @Query("SELECT uc FROM UserCodelab uc WHERE uc.user.role = :role AND uc.codelab.id = :codelabId")
    List<UserCodelab> findByUserRoleCodelabId(@Param("role") UserRole role, @Param("codelabId") Long codelabId);

    /** Get the list of usercodelab related to a specific course and user.
     * @see switchfully.lms.service.CourseService
     * */
    @Query(value= """
    SELECT uc.*
    FROM courses crs
    JOIN courses_modules cm ON crs.id = cm.course_id
    JOIN modules m ON cm.module_id = m.id
    JOIN modules_submodules ms ON m.id = ms.module_id
    JOIN submodules sm ON ms.submodule_id = sm.id
    JOIN codelabs cdl ON sm.id = cdl.submodule_id
    JOIN users_codelabs uc on cdl.id = uc.codelab_id
    WHERE crs.id = :courseId AND uc.user_id = :userId
    """, nativeQuery = true)
    List<UserCodelab> findProgressByCourseIdAndUserID(@Param("courseId") Long courseId, @Param("userId") Long userId);

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

    /** Get the list of usercodelab related to a specific submodule and user.
     * @see switchfully.lms.service.SubmoduleService
     * */
    @Query(value= """
    SELECT uc.*
    FROM submodules sm
    JOIN codelabs cdl ON sm.id = cdl.submodule_id
    JOIN users_codelabs uc on cdl.id = uc.codelab_id
    WHERE sm.id = :submodulesId AND uc.user_id = :userId
    """, nativeQuery = true)
    List<UserCodelab> findProgressBySubmodulesIdAndUserID(@Param("submodulesId") Long submodulesId, @Param("userId") Long userId);

}
