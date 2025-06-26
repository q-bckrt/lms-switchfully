package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;

import java.util.List;

/**
 * Repository interface for managing Codelab entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Codelab
 * @see JpaRepository
 */
@Repository
public interface CodelabRepository extends JpaRepository<Codelab, Long> {

    /** Get the list of codelab related to a specific class.
     * @see switchfully.lms.service.UserCodelabService
     * */
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

}
