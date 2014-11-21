package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "postnummer", indexes = { @Index(name="nummer", columnList="nummer") })
public class PostnummerEntity
        extends DobbeltHistorikBase<PostnummerEntity, PostnummerRegistreringEntity, PostnummerRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
    private int nummer;

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 36)
    private String navn;

    @OneToMany(mappedBy = "liggerIPostnummer")
    private Collection<AdgangspunktEntity> adgangspunkter;


    public int getNummer() {
        return this.nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Collection<AdgangspunktEntity> getAdgangspunkter() {
        return this.adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }


    public static PostnummerEntity create() {
        PostnummerEntity entity = new PostnummerEntity();
        entity.generateNewUUID();
        return entity;
    }

    protected PostnummerRegistreringEntity createRegistreringEntity(RegistreringEntity oioRegistrering, List<VirkningEntity> virkninger) {
        return new PostnummerRegistreringEntity(this, oioRegistrering, virkninger);
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.postnummerRepository;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) (long) this.getId();
    }

}
