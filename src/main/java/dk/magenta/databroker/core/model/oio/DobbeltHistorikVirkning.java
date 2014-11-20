package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public class DobbeltHistorikVirkning<
        E extends DobbeltHistorikBase<E, R, V>,
        R extends DobbeltHistorikRegistrering<E, R, V>,
        V extends DobbeltHistorikVirkning<E, R, V>
        > {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @OneToOne(optional = false)
    private VirkningEntity virkning;

    @ManyToOne
    private R entitetsRegistrering;

    public DobbeltHistorikVirkning() {
    }

    public DobbeltHistorikVirkning(R entitetsRegistrering, VirkningEntity virkning) {
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
