package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Course;
import switchfully.lms.domain.UserCodelab;

import java.util.List;

/**
 * Repository interface for managing Course entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Course
 * @see JpaRepository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

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
}
