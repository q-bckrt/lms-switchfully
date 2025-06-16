package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Course;

/**
 * Repository interface for managing Course entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Course
 * @see JpaRepository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
