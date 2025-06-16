package switchfully.lms.service.dto;

import java.util.Objects;

public class CommentInputDto {
    private String comment;

    public CommentInputDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentInputDto that = (CommentInputDto) o;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(comment);
    }

    @Override
    public String toString() {
        return "CommentInputDto{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
