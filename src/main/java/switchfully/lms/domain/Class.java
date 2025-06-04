package switchfully.lms.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static switchfully.lms.utility.validation.Validation.validateArgument;

@Entity
@Table(name = "classes")
public class Class {
    @Id
    @SequenceGenerator(sequenceName = "classes_seq", name = "classes_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classes_seq")
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "classes")
    private List<User> users = new ArrayList<>();

    public Class() {}
    public Class(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getUsers() {
        return users;
    }

    public User addCoach(User coach) {
        validateArgument(coach,"User must be of type COACH",u->!u.getRole().equals(UserRole.COACH));
        this.users.add(coach);
        return coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class that = (Class) o;
        return this.title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, users);
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", users=" + users +
                '}';
    }
}
