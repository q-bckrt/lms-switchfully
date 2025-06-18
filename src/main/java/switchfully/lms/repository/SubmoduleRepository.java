package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Submodule;
import switchfully.lms.domain.UserCodelab;

import java.util.List;

/**
 * Repository interface for managing Submodule entities.
 * It extends JpaRepository to provide CRUD operations.
 * @see Submodule
 * @see JpaRepository
 */
@Repository
public interface SubmoduleRepository extends JpaRepository<Submodule, Long> {

    /** Get the list of usercodelab related to a specific submodule and user.
     * @see switchfully.lms.service.SubmoduleService
     * */
    @Query(value= """
    SELECT uc.*
    FROM submodules sm
    JOIN codelabs cdl ON sm.id = cdl.submodule_id
    JOIN users_codelabs uc on cdl.id = uc.codelab_id
    WHERE sm.id = :submodulesId AND uc.user_id = :userId
    """, nativeQuery = true)
    List<UserCodelab> findProgressBySubmodulesIdAndUserID(@Param("submodulesId") Long submodulesId, @Param("userId") Long userId);
}
