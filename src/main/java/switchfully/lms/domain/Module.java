package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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

    @ManyToMany(mappedBy = "childModules")
    private List<Course> parentCourses = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "modules_submodules",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "submodule_id")
    )
    private List<Submodule> childSubmodules = new ArrayList<>();

    // CONSTRUCTORS
    public Module() {}

    public Module(String title) {
        this.title = title;
    }

    // METHODS
    public void addChildSubmodule(Submodule submodule) {
        this.childSubmodules.add(submodule);
    }
}