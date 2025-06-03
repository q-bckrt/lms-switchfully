package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    // FIELDS
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courses_seq")
    @SequenceGenerator(sequenceName = "courses_seq",name = "courses_seq", allocationSize = 1)
    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "courses_modules",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<Module> modules;


    // CONSTRUCTORS
    public Course() {
        // Default constructor for JPA
    }
    public Course(String title) {
        this.title = title;
    }
    public Course(String title, List<Module> modules) {
        this.title = title;
        this.modules = modules;
    }
}
