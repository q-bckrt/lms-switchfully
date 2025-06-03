package switchfully.lms.repository;

import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Course;

@Repository
public interface CourseRepository implements JpaRepository<Course, Long> {
}
