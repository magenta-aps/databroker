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
        >  {

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

    @OneToOne(optional = true)
    private R latestRegistrering;

    @OneToOne(optional = true)
    private R preferredRegistrering;

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

    private void setRegistreringer(Collection<R> registreringer) {
        this.registreringer = registreringer;
    }

    private void addToRegistreringer(R registrering) {
        this.registreringer.add(registrering);
    }

    public R getLatestRegistrering() {
        return latestRegistrering;
    }

    public void setLatestRegistrering(R latestRegistrering) {
        this.latestRegistrering = latestRegistrering;
    }

    public R getPreferredRegistrering() {
        return preferredRegistrering;
    }

    public void setPreferredRegistrering(R preferredRegistrering) {
        this.preferredRegistrering = preferredRegistrering;
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

    protected abstract R createRegistreringEntity();

    private R createRegistreringEntity(RegistreringEntity forOIORegistrering) {
        R newReg = this.createRegistreringEntity();
        newReg.setRegistrering(forOIORegistrering);
        return newReg;
    }

    public R addRegistrering(RegistreringEntity fromOIORegistrering) {
        return this.addRegistrering(fromOIORegistrering, null);
    }

    public R addRegistrering(RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger) {
        R newReg = this.createRegistreringEntity(fromOIORegistrering);
        if(virkninger != null) {
            for(VirkningEntity v : virkninger) {
                newReg.addToRegistreringsVirkninger(v);
            }
        }
        this.addToRegistreringer(newReg);
        if(
                this.latestRegistrering == null || fromOIORegistrering.getRegistreringFra().after(
                        this.latestRegistrering.getRegistrering().getRegistreringFra()
                )) {
            this.latestRegistrering = newReg;
        }
        return newReg;
    }


    /*
    public abstract JpaRepository getRepository(RepositoryCollection repositoryCollection);
    // Subclasses must implement their own logic, returning the correct item from the repositoryCollection

    public void save(RepositoryCollection repositories, RegistreringEntity oioReg) {
        this.save(repositories, oioReg, new ArrayList<VirkningEntity>());
    }
    public void save(RepositoryCollection repositories, RegistreringEntity oioReg, List<VirkningEntity> virkninger) {
        JpaRepository entityRepository = this.getRepository(repositories);
        this.addRegistrering(oioReg, virkninger);
        entityRepository.save(this);
    }
    */


}
