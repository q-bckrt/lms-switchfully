package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.User;

import java.util.List;

/**
 * Repository interface for managing Codelab entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Codelab
 * @see JpaRepository
 */
@Repository
public interface CodelabRepository extends JpaRepository<Codelab, Long> {

    @Query(value = """
    SELECT cdl.*
    FROM classes cls
    JOIN courses crs ON cls.course_id = crs.id
    JOIN courses_modules cm ON crs.id = cm.course_id
    JOIN modules m ON cm.module_id = m.id
    JOIN modules_submodules ms ON m.id = ms.module_id
    JOIN submodules sm ON ms.submodule_id = sm.id
    JOIN codelabs cdl ON sm.id = cdl.submodule_id
    WHERE cls.id = :classId
    """, nativeQuery = true)
    List<Codelab> findCodelabsByClassId(@Param("classId") Long classId);

    @Query(value = """
    SELECT u.*
    FROM codelabs cdl
    JOIN submodules sm ON cdl.submodule_id = sm.id
    JOIN modules_submodules ms ON sm.id = ms.submodule_id
    JOIN modules m ON ms.module_id = m.id
    JOIN courses_modules cm ON m.id = cm.module_id
    JOIN courses c ON cm.course_id = c.id
    JOIN classes cls ON c.id = cls.course_id
    JOIN users_classes ucls ON cls.id = ucls.class_id
    JOIN users u ON ucls.user_id = u.id
    WHERE cdl.id = :codelabId
    """, nativeQuery = true)
    List<User> findUsersByCodelabId(@Param("codelabId") Long codelabId);

}
