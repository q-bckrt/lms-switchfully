package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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
    private List<Module> childModules = new ArrayList<>();


    // CONSTRUCTORS
    public Course() {}
    public Course(String title) {
        this.title = title;
    }

    // METHODS
    public void addChildModule(Module module) {
        this.childModules.add(module);
    }
}
