package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "modules")
public class Module {

    // FIELDS
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modules_seq")
    @SequenceGenerator(sequenceName = "modules_seq", name = "modules_seq", allocationSize = 1)
    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "modules")
    private List<Course> courses;

    // CONSTRUCTORS
    public Module() {}
    public Module(String title) {
        this.title = title;
    }
    public Module(String title, List<Course> courses) {
        this.title = title;
        this.courses = courses;
    }
}
