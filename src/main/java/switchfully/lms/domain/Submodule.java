package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Submodule in the learning management system.
 * A Submodule is a component of a Module and can contain multiple Codelabs.
 * It represents a specific topic or section within a larger Module.
 * @see Module
 * @see Codelab
 */
@Data
@Entity
@Table(name = "submodules")
public class Submodule {

    // FIELDS
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submodules_seq")
    @SequenceGenerator(sequenceName = "submodules_seq", name = "submodules_seq", allocationSize = 1)
    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "childSubmodules")
    private List<Module> parentModules = new ArrayList<>();

    @OneToMany(mappedBy = "parentSubmodule", cascade = CascadeType.ALL)
    private List<Codelab> childCodelabs = new ArrayList<>();

    // CONSTRUCTORS
    // Default constructor required by JPA.
    public Submodule() {}

    /**
     * This constructor creates a new Submodule with the specified title.
     * A submodule is created independently and does not belong to anything at the time of creation,
     * but it is meant to be associated with Modules right after creation, and optionally with Codelabs.
     *
     * @param title The title of the Submodule
     */
    public Submodule(String title) {
        this.title = title;
    }

    // METHODS
    /**
     * Adds a Codelab to this Submodule's list of child Codelabs.
     * This method allows for the dynamic addition of Codelabs to a Submodule.
     * If the Codelab is already in the list, it is not added again.
     *
     * @param codelab The Codelab to add to this Submodule
     * @see Codelab
     */
    public void addChildCodelab(Codelab codelab) {
        if (!childCodelabs.contains(codelab)) {
            childCodelabs.add(codelab);
        }
    }

    // TO STRING
    @Override
    public String toString() {
        return "Submodule{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
