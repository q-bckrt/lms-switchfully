package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
