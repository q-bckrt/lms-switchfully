package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Codelab;
import switchfully.lms.domain.Comment;
import switchfully.lms.domain.User;
import switchfully.lms.service.dto.CommentInputDto;
import switchfully.lms.service.dto.CommentOutputDto;

/**
 * Mapper class responsible for converting between {@link Comment} domain objects
 * and their corresponding DTO representations: {@link CommentInputDto} and {@link CommentOutputDto}.
 * <p>
 * Used in the {@link switchfully.lms.service.CommentService} to isolate transformation logic
 * and maintain a clear separation between layers.
 *
 * @see Comment
 * @see CommentInputDto
 * @see CommentOutputDto
 */
@Component
public class CommentMapper {

    /**
     * Converts a {@link CommentInputDto} into a {@link Comment} domain object.
     *
     * @param user the {@link User} authoring the comment
     * @param codelab the {@link Codelab} the comment is associated with
     * @param input the input DTO containing the comment text
     * @return a new {@link Comment} object populated with the provided information
     */
    public Comment inputToComment(User user, Codelab codelab, CommentInputDto input) {
        return new Comment(user, codelab, input.getComment());
    }

    /**
     * Converts a {@link Comment} domain object into a {@link CommentOutputDto}.
     *
     * @param comment the domain object to be transformed
     * @return a {@link CommentOutputDto} representing the given comment
     */
    public CommentOutputDto commentToOutput(Comment comment) {
        return new CommentOutputDto(comment.getId(),
                comment.getUser().getDisplayName(),
                comment.getCodelab().getTitle(),
                comment.getComment(),
                comment.getTimeStamp());
    }
}
