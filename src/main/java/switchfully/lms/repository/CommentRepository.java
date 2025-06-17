package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
