package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Class;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    /** Searches the database for a class the provided title string
     * @param title title to query class repo with
     * */
    boolean existsByTitle(String title);

    /** Get the list of user related to a codelab.
     * @see switchfully.lms.service.UserCodelabService
     * */
    @Query(value = """
    SELECT cls.*
    FROM users u
    JOIN users_classes uc on u.id = uc.user_id
    JOIN classes cls on uc.class_id = cls.id
    WHERE u.user_name = :username
    """, nativeQuery = true)
    List<Class> findClassByUserName(@Param("username") String username);
}
