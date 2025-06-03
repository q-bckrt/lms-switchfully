package switchfully.lms.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Service;

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
    private List<Module> parentModules;

    // CONSTRUCTORS
    public Submodule() {}
    public Submodule(String title) {
        this.title = title;
    }
    public Submodule(String title, List<Module> parentModules) {
        this.title = title;
        this.parentModules = parentModules;
    }
}
