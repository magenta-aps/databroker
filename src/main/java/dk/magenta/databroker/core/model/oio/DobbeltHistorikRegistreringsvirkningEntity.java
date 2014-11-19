package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public class DobbeltHistorikRegistreringsvirkningEntity<
        E extends DobbeltHistorikEntity<E, R, V>,
        R extends DobbeltHistorikRegistreringEntity<E, R, V>,
        V extends DobbeltHistorikRegistreringsvirkningEntity<E, R, V>
        > {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @OneToOne(optional = false)
    private VirkningEntity virkning;

    @ManyToOne
    private R entitetsRegistrering;

    public DobbeltHistorikRegistreringsvirkningEntity() {
    }

    public DobbeltHistorikRegistreringsvirkningEntity(R entitetsRegistrering, VirkningEntity virkning) {
        this.entitetsRegistrering = entitetsRegistrering;
        this.virkning = virkning;
    }

    public Long getId() {
        return id;
    }

    public VirkningEntity getVirkning() {
        return virkning;
    }

    public void setVirkning(VirkningEntity virkning) {
        this.virkning = virkning;
    }

    public R getEntitetsRegistrering() {
        return entitetsRegistrering;
    }

    public void setEntitetsRegistrering(R entitetsRegistrering) {
        this.entitetsRegistrering = entitetsRegistrering;
    }
}
