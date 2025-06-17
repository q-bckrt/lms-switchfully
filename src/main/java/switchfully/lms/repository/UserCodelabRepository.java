package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.domain.UserCodelabId;
import switchfully.lms.domain.UserRole;

import java.util.List;

public interface UserCodelabRepository extends JpaRepository<UserCodelab, UserCodelabId> {

    List<UserCodelab> findByIdUserId(Long userId);
    List<UserCodelab> findByIdCodelabId(Long codelabId);

    Boolean existsByUserIdAndCodelabId(Long userId, Long codelabId);

    @Query("SELECT uc FROM UserCodelab uc WHERE uc.user.role = :role AND uc.codelab.id = :codelabId")
    List<UserCodelab> findByUserRoleCodelabId(@Param("role") UserRole role, @Param("codelabId") Long codelabId);
}
