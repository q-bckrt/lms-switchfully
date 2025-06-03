package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository implements JpaRepository<Module, Long> {
}
