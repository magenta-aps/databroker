package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/24/14.
 */
@MappedSuperclass
public abstract class OioRegistreringBase<
        E extends OioEntityBase<E, R>,
        R extends OioRegistreringBase<E, R>
        > {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;
    @OneToOne
    private RegistreringEntity registreringsData;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn( name="reg_id"),
            inverseJoinColumns = @JoinColumn( name="virk_id")
    )
    private Collection<VirkningEntity> virkninger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // This should be mapped to the as a hibernate @ManyToOne relation
    public abstract E getEntitet();

    public RegistreringEntity getRegistreringsData() {
        return registreringsData;
    }

    public void setRegistreringsData(RegistreringEntity registreringsData) {
        this.registreringsData = registreringsData;
    }

    protected Collection<VirkningEntity> getVirkninger() {
        return virkninger;
    }

    protected void setVirkninger(Collection<VirkningEntity> virkninger) {
        this.virkninger = virkninger;
    }
}
