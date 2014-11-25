package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adresse", indexes = { @Index(name="husnummer", columnList="husnummer_id") })
public class AdresseEntity
        extends DobbeltHistorikBase<AdresseEntity, AdresseVersionEntity>
        implements Serializable {

    @OneToMany(mappedBy = "entitet")
    private Collection<AdresseVersionEntity> versions;

    @OneToOne
    private AdresseVersionEntity latestVersion;
    @OneToOne
    private AdresseVersionEntity preferredVersion;

    protected AdresseEntity() {
        this.versions = new ArrayList<AdresseVersionEntity>();
    }

    public static AdresseEntity create() {
        AdresseEntity entity = new AdresseEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.adresseRepository;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        AdresseEntity that = (AdresseEntity) other;

        if (this.getId() != that.getId()) {
            return false;
        }
        if (this.getUuid() != null ? !this.getUuid().equals(that.getUuid()) : that.getUuid() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        return (int) result;
    }

    @Override
    public Collection<AdresseVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public AdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdresseVersionEntity newLatest) {
        this.latestVersion = newLatest;
    }

    @Override
    public AdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdresseVersionEntity newPreferred) {
        this.preferredVersion = newPreferred;
    }

    @Override
    protected AdresseVersionEntity createVersionEntity() {
        return new AdresseVersionEntity(this);
    }
}
