package switchfully.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import switchfully.lms.domain.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    /** Searches the database for a class the provided title string
     * @param title title to query class repo with
     * */
    boolean existsByTitle(String title);
}
