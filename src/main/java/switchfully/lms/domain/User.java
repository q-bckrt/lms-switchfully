package switchfully.lms.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="users")
public class User {
    @Id
    @SequenceGenerator(sequenceName = "users_seq", name="users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    @Column(name="user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /** class of type List<Class> holds the class entities from the database that are linked to this user, it initialized as null upon user creation and is set later.
     * @see Class */
    @ManyToMany
    @JoinTable(name="users_classes",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name="class_id")})
    private List<Class> classes = new ArrayList<>();

    public User() {
    }

    public User(String userName, String displayName, String firstName, String lastName, String email, UserRole role) {
        this.userName = userName;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }


    public UserRole getRole() {
        return role;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    /** Adds a Class to the classes field.
     *
     * @param classDomain
     */
    public void addClasses(Class classDomain) {
        this.classes.add(classDomain);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", classes=" + classes +
                '}';
    }
}
