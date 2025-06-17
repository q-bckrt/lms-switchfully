package switchfully.lms.domain;

import jakarta.persistence.*;

@Entity
@Table(name="users_codelabs")
public class UserCodelab {

    @EmbeddedId
    private UserCodelabId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codelabId")
    @JoinColumn(name = "codelab_id")
    private Codelab codelab;

    @Column(name="progress", nullable=false)
    @Enumerated(EnumType.STRING)
    private ProgressLevel progressLevel;

    public UserCodelab() {
    }

    public UserCodelab(User user, Codelab codelab, ProgressLevel progressLevel) {
        this.id = new UserCodelabId(user.getId(), codelab.getId());
        this.user = user;
        this.codelab = codelab;
        this.progressLevel = progressLevel;
    }

    public UserCodelabId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Codelab getCodelab() {
        return codelab;
    }

    public ProgressLevel getProgressLevel() {
        return progressLevel;
    }

    public void setProgressLevel(ProgressLevel progressLevel) {
        this.progressLevel = progressLevel;
    }
}
