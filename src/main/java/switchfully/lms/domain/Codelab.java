package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.*;

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
    public Codelab() {}

    public Codelab(String title, String details, Submodule parentSubmodule) {
        this.title = title;
        this.details = details;
        this.parentSubmodule = parentSubmodule;
    }
}
