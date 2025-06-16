package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Course in the learning management system.
 * A Course is a top-level entity that doesn't have to belong to anything,
 * but that is meant to be associated with classes and is a collection of Modules.
 * (Many-to-Many relationship). It only has a title and a list of child Modules.
 * @see Module
 * @see Class
 */
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
    // Default constructor required by JPA.
    public Course() {}

    /**
     * This constructor creates a new Course with the specified title.
     * Since a Course is a top-level entity, it does not have to belong to anything
     * at the time of creation, but it is meant to 'contain' modules and be associated with classes.
     *
     * @param title The title of the Course
     */
    public Course(String title) {
        this.title = title;
    }

    // METHODS
    /**
     * Adds a Module to the Course's list of child Modules.
     * This method allows for the dynamic addition of Modules to a Course.
     *
     * @param module The Module to be added to the Course
     * @see Module
     */
    public void addChildModule(Module module) {
        this.childModules.add(module);
    }
}
