package switchfully.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import switchfully.lms.domain.*;
import switchfully.lms.repository.*;
import switchfully.lms.service.dto.CommentInputDto;
import switchfully.lms.service.dto.CommentOutputDto;
import switchfully.lms.service.mapper.CommentMapper;
import switchfully.lms.utility.exception.InvalidInputException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CodelabRepository codelabRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private UserCodelabRepository userCodelabRepository;
    @Mock
    private SubmoduleRepository submoduleRepository;

    private Comment testComment;
    private User testUser;
    private Submodule testSubmodule;
    private Codelab testCodelab;

    public static void setId(Object target, Long idValue) {
        try {
            Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, idValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field id via reflection", e);
        }
    }


    @BeforeEach
    public void beforeEach() {

        testUser = new User("username","displayname","firstname","lastname","email@email.com", UserRole.STUDENT);
        testSubmodule = new Submodule("submodule title");
        testCodelab = new Codelab("codelabtitle","codelab details",testSubmodule);
        testComment = new Comment(testUser,testCodelab,"test comment");

        setId(testUser,1L);
        setId(testSubmodule,2L);
        setId(testCodelab,3L);
        setId(testComment,4L);

        lenient().when(userRepository.findByUserName(testUser.getUserName())).thenReturn(testUser);
        lenient().when(submoduleRepository.findById(testSubmodule.getId())).thenReturn(Optional.of(testSubmodule));
        lenient().when(codelabRepository.findById(testCodelab.getId())).thenReturn(Optional.of(testCodelab));
        lenient().when(commentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));

        lenient().when(userRepository.existsByUserName(testUser.getUserName())).thenReturn(true);
        lenient().when(userCodelabRepository.existsByUserIdAndCodelabId(testUser.getId(), testCodelab.getId())).thenReturn(true);

    }

    @Test
    void givenUserCodelabExistsAndInputValid_whenPostComment_thenReturnCommentDto() {
        CommentInputDto input = new CommentInputDto("new test comment");
        Comment comment = new Comment(testUser,testCodelab,"new test comment");
        setId(comment,99L);
        UserCodelab userCodelab = new UserCodelab(testUser,testCodelab,ProgressLevel.NOT_STARTED);
        userCodelabRepository.save(userCodelab);

        //EXPECTED
        CommentOutputDto expected = new CommentOutputDto(99L,testUser.getDisplayName(),testCodelab.getTitle(),input.getComment(), LocalDateTime.now());

        when(commentMapper.inputToComment(testUser,testCodelab,input)).thenReturn(comment);
        when(commentMapper.commentToOutput(comment)).thenReturn(expected);


        //RESULT
        CommentOutputDto result = commentService.postComment(input,testUser.getUserName(),testCodelab.getId());

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void givenUserCodelabExistsAndInputInvalid_whenPostComment_thenThrowException() {
        CommentInputDto input = new CommentInputDto("  ");

        assertThrows(InvalidInputException.class, () -> commentService.postComment(input,testUser.getUserName(),testCodelab.getId()));
    }

    @Test
    void givenUserNameDoesNotExistsAndInputValid_whenPostComment_thenThrowException() {
        CommentInputDto input = new CommentInputDto("valid input");
        String invalidUsername = "invalidUsername";


        assertThrows(InvalidInputException.class, () -> commentService.postComment(input,invalidUsername,testCodelab.getId()));
    }

    @Test
    void givenUserCodelabDoesNotExistsAndInputValid_whenPostComment_thenThrowException() {
        CommentInputDto input = new CommentInputDto("Valid Input");
        User newUser = new User("newusername","displayname","firstname","lastname","newemail@yahoo.com", UserRole.STUDENT);
        setId(newUser,89L);


        assertThrows(InvalidInputException.class, () -> commentService.postComment(input,newUser.getUserName(),testCodelab.getId()));
    }

    @Test
    void givenCodelabDoesNotExistsAndInputValid_whenPostComment_thenThrowException() {
        CommentInputDto input = new CommentInputDto("Valid Input");
        Codelab newCodelab = new Codelab("codelabtitle","codelab details",testSubmodule);
        setId(newCodelab,89L);


        assertThrows(InvalidInputException.class, () -> commentService.postComment(input,testUser.getUserName(),newCodelab.getId()));
    }


}
