package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Submodule;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodule, Long> {
}
