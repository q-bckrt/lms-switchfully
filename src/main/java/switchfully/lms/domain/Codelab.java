package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "codelabs")
public class Codelab {

    // FIELDS
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codelabs_seq")
    @SequenceGenerator(sequenceName = "codelabs_seq", name = "codelabs_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JoinColumn(name = "submodule_id")
    private Submodule parentSubmodule;

}
