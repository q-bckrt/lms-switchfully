package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a Codelab in the learning management system.
 * A Codelab is an exercise that belongs to a Submodule (Many-to-One relationship).
 * It contains a title, details, and a reference to its parent Submodule.
 * @see Submodule
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "codelabs")
public class Codelab {

    // FIELDS
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codelabs_seq")
    @SequenceGenerator(sequenceName = "codelabs_seq", name = "codelabs_seq", allocationSize = 1)
    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JoinColumn(name = "submodule_id")
    private Submodule parentSubmodule;

    // CONSTRUCTORS
    // Default constructor required by JPA.
    public Codelab() {}

    /**
     * This constructor creates a new Codelab with the specified title, details, and parent submodule.
     *
     * @param title The title of the Codelab
     * @param details Additional details or description of the Codelab
     * @param parentSubmodule The Submodule that this Codelab belongs to
     * @see Submodule
     */
    public Codelab(String title, String details, Submodule parentSubmodule) {
        this.title = title;
        this.details = details;
        this.parentSubmodule = parentSubmodule;
    }
}