package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
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

    @ManyToMany(mappedBy = "childModules")
    private List<Course> parentCourses;

    @ManyToMany
    @JoinTable(
            name = "modules_submodules",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "submodule_id")
    )
    private List<Submodule> childSubmodules;

    // CONSTRUCTORS
    public Module() {}
    public Module(String title) {
        this.title = title;
    }
    public Module(String title, List<Course> parentCourses) {
        this.title = title;
        this.parentCourses = parentCourses;
    }
    public Module(String title, List<Course> parentCourses, List<Submodule> childSubmodules) {
        this.title = title;
        this.parentCourses = parentCourses;
        this.childSubmodules = childSubmodules;
    }
}