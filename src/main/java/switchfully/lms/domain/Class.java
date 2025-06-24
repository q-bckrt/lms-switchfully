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

    /** course of type Course holds the course entity from the database that is linked to this class, it initialized as null upon class creation and is set later.
     * @see Course */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /** users of type List<User> holds the users entities from the database that are enrolled in this class, it initialized as null upon class creation and is set later at multiple stages.
     * @see User */
    @ManyToMany(mappedBy = "classes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    public Course getCourse() {
        return course;
    }

    /** Adds a User to the users field, only if the user has UserRole.COACH as role
     * @param coach User object with Coach role
     * @throws IllegalArgumentException if {@code coach} does not have UserRole.COACH
     * */
    public User addCoach(User coach) {
        validateArgument(coach,"User must be of type COACH",u->!u.getRole().equals(UserRole.COACH));
        this.users.add(coach);
        return coach;
    }

    /** setter for course field in this class
     * @param course Course object to set course field to
     * */
    public Course setCourse(Course course) {
        this.course = course;
        return course;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
                ", users=" + users.stream().map(User::getUserName).toList() +
                '}';
    }
}
