package switchfully.lms.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    @SequenceGenerator(sequenceName = "comments_seq", name = "comments_seq", allocationSize = 1)
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "codelab_id", nullable = false)
    private Codelab codelab;

    @Column(name = "comment")
    String comment;

    @Column(name = "time_stamp")
    LocalDateTime timeStamp;

    public Comment() {}
    public Comment(User user, Codelab codelab, String comment) {
        this.user = user;
        this.codelab = codelab;
        this.comment = comment;
        this.timeStamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Codelab getCodelab() {
        return codelab;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /** set the comment field once, only when its null or empty
     *
     * @param text the text content of the comment
     * @return the text content
     */
    public String setTextOnce(String text) {
        if(this.comment == null || this.comment.isEmpty()) {
            this.comment = text;
        }
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id) && Objects.equals(user, comment1.user) && Objects.equals(codelab, comment1.codelab) && Objects.equals(comment, comment1.comment) && Objects.equals(timeStamp, comment1.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, codelab, comment, timeStamp);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", codelab=" + codelab +
                ", comment='" + comment + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
