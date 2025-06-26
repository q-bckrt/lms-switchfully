package switchfully.lms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import switchfully.lms.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodelabRepository codelabRepository;
    @Autowired
    private SubmoduleRepository submoduleRepository;

    private Comment testComment;
    private User testUser;
    private Submodule testSubmodule;
    private Codelab testCodelab;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        codelabRepository.deleteAll();
        submoduleRepository.deleteAll();

        testUser = userRepository.save(new User("username","displayname","firstname","lastname","email@email.com",UserRole.STUDENT));
        testSubmodule = submoduleRepository.save(new Submodule("submodule title"));
        testCodelab = codelabRepository.save(new Codelab("codelabtitle","codelab details",testSubmodule));
        testComment = commentRepository.save(new Comment(testUser,testCodelab,"test comment"));
        commentRepository.flush();
        userRepository.flush();
        codelabRepository.flush();
        submoduleRepository.flush();
    }

    @Test
    void givenCommentExistInRepo_findById_thenReturnComment() {
        Comment comment = commentRepository.findById(testComment.getId()).get();

        assertThat(comment.getId()).isEqualTo(testComment.getId());
        assertThat(comment.getComment()).isEqualTo(testComment.getComment());
        assertThat(comment.getUser()).isEqualTo(testComment.getUser());
        assertThat(comment.getCodelab()).isEqualTo(testComment.getCodelab());
    }

    @Test
    void givenCorrectInput_whenSave_thenReturnCorrectClass() {
        Comment newComment = new Comment(testUser,testCodelab,"new test comment");
        Comment savedComment = commentRepository.save(newComment);

        assertThat(savedComment.getId()).isEqualTo(newComment.getId());
        assertThat(savedComment.getComment()).isEqualTo(newComment.getComment());
        assertThat(savedComment.getUser()).isEqualTo(newComment.getUser());
        assertThat(savedComment.getCodelab()).isEqualTo(newComment.getCodelab());
    }

    @Test
    void givenInvalidTitle_whenSave_thenThrowException() {
        Comment newComment = new Comment(null,null,null);

        assertThrows(Exception.class, () -> commentRepository.save(newComment));
    }

}
