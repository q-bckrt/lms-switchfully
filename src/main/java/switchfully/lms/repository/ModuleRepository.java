package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Module;

/**
 * Repository interface for managing Module entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Module
 * @see JpaRepository
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
