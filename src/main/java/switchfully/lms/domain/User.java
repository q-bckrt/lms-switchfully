package switchfully.lms.domain;

import jakarta.persistence.*;

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

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany
    @JoinTable(name="users_classes",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name="class_id")})
    private List<Class> classes;

    public User() {
    }

    public User(String userName, String displayName, String email, String password, UserRole role) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
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
