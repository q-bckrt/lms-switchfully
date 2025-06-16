package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.domain.UserCodelabId;

import java.util.List;

public interface UserCodelabRepository extends JpaRepository<UserCodelab, UserCodelabId> {

    List<UserCodelab> findByIdUserId(Long userId);
    List<UserCodelab> findByIdCodelabId(Long codelabId);
}
