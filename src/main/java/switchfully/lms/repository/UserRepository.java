package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /** Searches and retrieve from the database a user with specific email.
     * @param email email to query user repository with
     * */
    User findByEmail(String email);
    /** Searches and retrieve from the database a user with specific username.
     * @param username username to query user repository with
     * */
    User findByUserName(String username);
    /** Searches the database for a user with specific email, used to assess uniqueness of email before registration of a new user.
     * @param email email to query user repository with
     * */
    boolean existsByEmail(String email);
    /** Searches the database for a user with specific username, used to assess uniqueness of username before registration of a new user.
     * @param username username to query user repository with
     * */
    boolean existsByUserName(String username);

    /** Get the list of user related to a codelab.
     * @see switchfully.lms.service.UserCodelabService
     * */
    @Query(value = """
    SELECT u.*
    FROM codelabs cdl
    JOIN submodules sm ON cdl.submodule_id = sm.id
    JOIN modules_submodules ms ON sm.id = ms.submodule_id
    JOIN modules m ON ms.module_id = m.id
    JOIN courses_modules cm ON m.id = cm.module_id
    JOIN courses c ON cm.course_id = c.id
    JOIN classes cls ON c.id = cls.course_id
    JOIN users_classes ucls ON cls.id = ucls.class_id
    JOIN users u ON ucls.user_id = u.id
    WHERE cdl.id = :codelabId
    """, nativeQuery = true)
    List<User> findUsersByCodelabId(@Param("codelabId") Long codelabId);

}
