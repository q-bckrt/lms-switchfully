package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.ProgressLevel;
import switchfully.lms.domain.User;
import switchfully.lms.domain.UserCodelab;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.UserCodelabRepository;
import switchfully.lms.service.mapper.UserMapper;
import switchfully.lms.utility.validation.Validation;

import java.util.List;

/**
 * Service class responsible for handling usercodelab-related operations such as update link between a user
 * and a codelab. <p>
 *
 * This service is there mainly to create/update a link between a user and a codelab. <p>
 *
 * Current functionality includes:
 * <ul>
 *   <li>Update link between user and codelab when a user is added to a class</li>
 *   <li>Update link between user and codelab when a new codelab is created</li>
 * </ul>
 */
@Service
public class UserCodelabService {

    private final UserCodelabRepository userCodelabRepository;
    private final CodelabRepository codelabRepository;

    public UserCodelabService(UserCodelabRepository userCodelabRepository, CodelabRepository codelabRepository) {
        this.userCodelabRepository = userCodelabRepository;
        this.codelabRepository = codelabRepository;
    }

    /** Update link between user and codelab when a user is added to a class.
     * Check that a link between a codelab and user do not already exist to avoid duplication.
     * @param user User for which link with codelabs must be created
     * @param classId id of the class the user has been added to
     * @see CodelabRepository
     * @see UserCodelabRepository
     * */
    public void updateLinkBetweenUserAndCodelabWithClassId(User user, Long classId) {
        // Fetch all codelabs linked to this class (via custom query or native SQL)
        List<Codelab> codelabs = codelabRepository.findCodelabsByClassId(classId);

        // Insert into users_codelabs table
        for (Codelab codelab : codelabs) {
            if (!userCodelabRepository.existsByUserIdAndCodelabId(user.getId(), codelab.getId())) {
                UserCodelab usersCodelab = new UserCodelab(user, codelab, ProgressLevel.NOT_STARTED);
                userCodelabRepository.save(usersCodelab);
            }
        }
    }

    /** Update link between user and codelab when a new codelab is created.
     * Check that a link between a codelab and user do not already exist to avoid duplication.
     * Get the list of user related to the submodule the codelab has been added to.
     * @param codelab new codelab created
     * @see CodelabRepository
     * @see UserCodelabRepository
     * */
    public void updateLinkBetweenUserAndCodelabWithCodelab( Codelab codelab) {
        //fetch all user of for the new codelab link to a class
        List<User> users = codelabRepository.findUsersByCodelabId(codelab.getId());

        for (User user : users) {
            if (!userCodelabRepository.existsByUserIdAndCodelabId(user.getId(), codelab.getId())) {
                UserCodelab userCodelab = new UserCodelab(user, codelab, ProgressLevel.NOT_STARTED);
                userCodelabRepository.save(userCodelab);
            }
        }
    }
}
