package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    public Submodule() {}

    public Submodule(String title) {
        this.title = title;
    }

    // METHODS
    public void addChildCodelab(Codelab codelab) {
        if (!childCodelabs.contains(codelab)) {
        }
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }
}
