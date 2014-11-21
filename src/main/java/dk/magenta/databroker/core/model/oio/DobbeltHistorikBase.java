package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public abstract class DobbeltHistorikBase<
        E extends DobbeltHistorikBase<E, R, V>,
        R extends DobbeltHistorikRegistrering<E, R, V>,
        V extends DobbeltHistorikVirkning<E, R, V>
        > {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true, unique = true)
    private String uuid;

    @Column(nullable = true, insertable = true, updatable = true)
    private String brugervendtNoegle;

    @OneToMany(mappedBy = "entitet", cascade = CascadeType.ALL)
    private Collection<R> registreringer;


    public DobbeltHistorikBase() {
        this.registreringer = new ArrayList<R>();
    }

    public DobbeltHistorikBase(String uuid, String brugervendtNoegle) {
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

    public R addToRegistreringer(RegistreringEntity reg, Collection<VirkningEntity> virkninger) {
        R dhReg = (R) new DobbeltHistorikRegistrering<E, R, V>((E)this, reg, virkninger);
        this.registreringer.add(dhReg);
        return dhReg;
    }

    public void generateNewUUID() {
        this.uuid = UUID.randomUUID().toString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        DobbeltHistorikBase that = (DobbeltHistorikBase) other;
        if (this.getId() != that.getId()) {
            return false;
        }
        if (this.getUuid() != null ? !this.getUuid().equals(that.getUuid()) : that.getUuid() != null) {
            return false;
        }
        return true;
    }



    public void save(RepositoryCollection repositories, RegistreringEntity oioReg) {
        this.save(repositories, oioReg, new ArrayList<VirkningEntity>());
    }
    public void save(RepositoryCollection repositories, RegistreringEntity oioReg, List<VirkningEntity> virkninger) {
        JpaRepository entityRepository = this.getRepository(repositories);
        this.addToRegistreringer(this.createRegistreringEntity(oioReg, virkninger));
        entityRepository.save(this);
    }

    protected abstract R createRegistreringEntity(RegistreringEntity oioRegistrering, List<VirkningEntity> virkninger);
    // Subclasses must implement their own logic, creating a new R with the given parameters

    public abstract JpaRepository getRepository(RepositoryCollection repositoryCollection);
    // Subclasses must implement their own logic, returning the correct item from the repositoryCollection
}
