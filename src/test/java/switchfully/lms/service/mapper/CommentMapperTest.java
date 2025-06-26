package switchfully.lms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import switchfully.lms.domain.*;
import switchfully.lms.service.dto.*;


import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapper();

    User testUser;
    Codelab testCodelab;
    Submodule testSubmodule;
    Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = new User("username","displayname","firstname","lastname","email@email.com",UserRole.STUDENT);
        testSubmodule = new Submodule("submodule title");
        testCodelab = new Codelab("codelabtitle","codelab details",testSubmodule);
        testComment = new Comment(testUser,testCodelab,"test comment");
    }

    @Test
    void givenValidCommentInputDto_whenInputToComment_thenReturnCommentDomain() {
        //GIVEN
        CommentInputDto input = new CommentInputDto("validcomment");
        //EXPECTED
        Comment expectedResult = new Comment(testUser,testCodelab,"validcomment");

        Comment result = commentMapper.inputToComment(testUser,testCodelab,input);

        assertThat(result.getComment()).isEqualTo(expectedResult.getComment());
    }

    @Test
    void givenCommentDomainEntity_whenCommentToOutput_thenReturnCommentDto() {
        //GIVEN
        Comment commentDomain = new Comment(testUser,testCodelab,"validcomment");
        //EXPECTED
        CommentOutputDto expectedResult = new CommentOutputDto(1L,testUser.getDisplayName(),testCodelab.getTitle(),commentDomain.getComment(), commentDomain.getTimeStamp());

        CommentOutputDto result = commentMapper.commentToOutput(commentDomain);

        assertThat(result.getComment()).isEqualTo(expectedResult.getComment());
        assertThat(result.getTimeStamp()).isEqualTo(expectedResult.getTimeStamp());
        assertThat(result.getCodelabTitle()).isEqualTo(expectedResult.getCodelabTitle());
        assertThat(result.getUserDisplayName()).isEqualTo(expectedResult.getUserDisplayName());
    }

}
