package switchfully.lms.service.dto;

import switchfully.lms.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentOutputDto {
    private Long id;
    private String userDisplayName;
    private String codelabTitle;
    private String comment;
    private LocalDateTime timeStamp;

    public CommentOutputDto(Long id, String userDisplayName, String codelabTitle, String comment, LocalDateTime timeStamp) {
        this.id = id;
        this.userDisplayName = userDisplayName;
        this.codelabTitle = codelabTitle;
        this.comment = comment;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getCodelabTitle() {
        return codelabTitle;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentOutputDto that = (CommentOutputDto) o;
        return Objects.equals(id, that.id) && Objects.equals(userDisplayName, that.userDisplayName) && Objects.equals(codelabTitle, that.codelabTitle) && Objects.equals(comment, that.comment) && Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userDisplayName, codelabTitle, comment, timeStamp);
    }

    @Override
    public String toString() {
        return "CommentOutputDto{" +
                "id=" + id +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", codelabTitle='" + codelabTitle + '\'' +
                ", comment='" + comment + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
