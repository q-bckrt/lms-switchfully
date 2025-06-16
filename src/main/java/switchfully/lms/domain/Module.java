package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Module in the learning management system.
 * A Module is a collection of Submodules and can be associated with multiple Courses.
 * (Many-to-Many relationships with both Courses and modules).
 * @see Course
 * @see Submodule
 */
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
    // Default constructor required by JPA.
    public Module() {}


    /**
     * This constructor creates a new Module with the specified title.
     * A module is created independently and does not belong to anything at the time of creation,
     * but it is meant to be associated with Courses and Submodules right after creation.
     *
     * @param title The title of the Course
     */
    public Module(String title) {
        this.title = title;
    }

    // METHODS
    /**
     * Adds a Submodule to the Module's list of child Submodules.
     * This method allows for the dynamic addition of Submodules to a Module.
     *
     * @param submodule The Submodule to be added to the Module
     */
    public void addChildSubmodule(Submodule submodule) {
        this.childSubmodules.add(submodule);
    }
}