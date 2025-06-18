package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Submodule;


/**
 * Repository interface for managing Submodule entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Submodule
 * @see JpaRepository
 */
@Repository
public interface SubmoduleRepository extends JpaRepository<Submodule, Long> {

}
