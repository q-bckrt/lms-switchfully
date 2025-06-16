package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;

/**
 * Repository interface for managing Codelab entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Codelab
 * @see JpaRepository
 */
@Repository
public interface CodelabRepository extends JpaRepository<Codelab, Long> {
}
