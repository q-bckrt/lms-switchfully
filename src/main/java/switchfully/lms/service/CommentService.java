package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Comment;
import switchfully.lms.domain.User;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.CommentRepository;
import switchfully.lms.repository.UserCodelabRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.CommentInputDto;
import switchfully.lms.service.dto.CommentOutputDto;
import switchfully.lms.service.mapper.CommentMapper;
import switchfully.lms.utility.exception.InvalidInputException;

import static switchfully.lms.utility.validation.Validation.validateArgument;
import static switchfully.lms.utility.validation.Validation.validateNonBlank;

/**
 * Service class for managing {@link Comment} entities.
 * <p>
 * Handles validation and business logic for posting comments on {@link Codelab}s by {@link User}s.
 * Validates input fields and ownership before saving the comment to the database.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Validates non-blank comment content</li>
 *     <li>Ensures the user and codelab exist before associating the comment</li>
 *     <li>Maps and persists {@link Comment} entities</li>
 * </ul>
 *
 * @see Comment
 * @see CommentRepository
 * @see CommentMapper
 * @see UserRepository
 * @see CodelabRepository
 * @see switchfully.lms.utility.exception.InvalidInputException
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CodelabRepository codelabRepository;
    private final CommentMapper commentMapper;
    private final UserCodelabRepository userCodelabRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          CodelabRepository codelabRepository,
                          CommentMapper commentMapper,
                          UserCodelabRepository userCodelabRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.codelabRepository = codelabRepository;
        this.commentMapper = commentMapper;
        this.userCodelabRepository = userCodelabRepository;
    }

    /**
     * Posts a comment on a specific {@link Codelab} by a given {@link User}.
     * <p>
     * Validates the comment input to ensure it's not blank, and verifies that the user and codelab exist.
     * Converts the input DTO into a domain model, persists it, and returns a mapped output DTO.
     *
     * @param commentInputDto the {@link CommentInputDto} containing the comment content
     * @param username the username of the {@link User} posting the comment
     * @param codelabId the ID of the {@link Codelab} the comment is associated with
     * @return {@link CommentOutputDto} representing the saved comment
     *
     * @throws InvalidInputException if the comment is blank, the user does not exist, or the codelab is not found
     *
     * @see Comment
     * @see CommentRepository
     * @see CommentMapper
     * @see User
     * @see Codelab
     * @see switchfully.lms.utility.exception.InvalidInputException
     */
    public CommentOutputDto postComment(CommentInputDto commentInputDto, String username, Long codelabId) {
        validateNonBlank(commentInputDto.getComment(),"Cannot post blank comments",InvalidInputException::new);
        validateArgument(username,"username " +username +" not found in repository",u->!userRepository.existsByUserName(u), InvalidInputException::new);

        //A COACH CAN ADDA COMMENT TO A CODELAB --> THIS IMPLEIS THAT A COACH ALSO HAS PROGRESS IN A CODELAB SINCE USERS CODELAB LINK MUST
        //EXIST FOR A USER TO BE ABLE TO PERSIST A COMMENT
        User user = userRepository.findByUserName(username);
        //ADD EXISTS BY USER ID IN USERCODELABREPO!!!!!!
        //validateArgument(user, "The codelab is not linked to this user",u->userCodelabRepository.existsByUserId,InvalidInputException::new);

        Codelab codelab = codelabRepository.findById(codelabId).orElseThrow(() -> new InvalidInputException("code lab " +codelabId+ " not found"));
        Comment comment = commentMapper.inputToComment(user, codelab, commentInputDto);
        commentRepository.save(comment);

        return commentMapper.commentToOutput(comment);
    }
}
