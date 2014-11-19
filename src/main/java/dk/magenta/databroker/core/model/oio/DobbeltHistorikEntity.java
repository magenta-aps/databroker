package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public abstract class DobbeltHistorikEntity<
        E extends DobbeltHistorikEntity<E, R, V>,
        R extends DobbeltHistorikRegistreringEntity<E, R, V>,
        V extends DobbeltHistorikRegistreringsvirkningEntity<E, R, V>
        > {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true)
    private String uuid;


    @Column(nullable = true, insertable = true, updatable = true)
    private String brugervendtNoegle;

    @OneToMany(mappedBy = "entitet")
    private Collection<R> registreringer;

    public DobbeltHistorikEntity(String uuid, String brugervendtNoegle) {
        this.uuid = uuid;
        this.brugervendtNoegle = brugervendtNoegle;
        this.registreringer = new ArrayList<R>();
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBrugervendtNoegle() {
        return brugervendtNoegle;
    }

    public void setBrugervendtNoegle(String brugervendtNoegle) {
        this.brugervendtNoegle = brugervendtNoegle;
    }

    public Collection<R> getRegistreringer() {
        return registreringer;
    }

    public void setRegistreringer(Collection<R> registreringer) {
        this.registreringer = registreringer;
    }

    public void addToRegistreringer(R registrering) {
        this.registreringer.add(registrering);
    }

    public void addToRegistreringer(RegistreringEntity reg, Collection<VirkningEntity> virkninger) {
        R dhReg = (R)new DobbeltHistorikRegistreringEntity<E, R, V>((E)this, reg, virkninger);
        this.registreringer.add(dhReg);
    }
}
