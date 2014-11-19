package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "core_oio_registrering_livscyklus")
public class RegistreringLivscyklusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    private String navn;

    @OneToMany(mappedBy = "livscyklus")
    private Collection<RegistreringEntity> registreringer;

    public RegistreringLivscyklusEntity(String navn) {
        this.navn = navn;
    }


    public Long getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
