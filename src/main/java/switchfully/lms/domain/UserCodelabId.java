package switchfully.lms.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserCodelabId implements Serializable {

    private Long userId;
    private Long codelabId;

    public UserCodelabId() {

    }

    public UserCodelabId(Long userId, Long codelabId) {
        this.userId = userId;
        this.codelabId = codelabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCodelabId)) return false;
        UserCodelabId that = (UserCodelabId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(codelabId, that.codelabId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, codelabId);
    }
}
