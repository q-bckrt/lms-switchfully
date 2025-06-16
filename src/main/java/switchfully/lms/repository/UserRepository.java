package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.User;

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

}
