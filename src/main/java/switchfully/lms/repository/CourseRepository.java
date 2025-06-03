package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
