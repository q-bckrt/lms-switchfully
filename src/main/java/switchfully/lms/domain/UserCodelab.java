package switchfully.lms.domain;

import jakarta.persistence.*;

@Entity
@Table(name="users_codelabs")
public class UserCodelab {

    @EmbeddedId
    private UserCodelabId id;

    @Column(name="progress", nullable=false)
    @Enumerated(EnumType.STRING)
    private ProgressLevel progressLevel;

    public UserCodelab() {
    }

    public UserCodelab(UserCodelabId id, ProgressLevel progressLevel) {
        this.id = id;
        this.progressLevel = progressLevel;
    }
}
