package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
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
@Table(name = "adgangspunkt")
public class AdgangspunktEntity
        extends DobbeltHistorikBase<AdgangspunktEntity, AdgangspunktVersionEntity>
        implements Serializable {

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private Collection<AdgangspunktVersionEntity> versions;

    @OneToOne
    private AdgangspunktVersionEntity latestVersion;

    @OneToOne
    private AdgangspunktVersionEntity preferredVersion;



    protected AdgangspunktEntity() {
        this.versions = new ArrayList<AdgangspunktVersionEntity>();
    }

    public static AdgangspunktEntity create() {
        AdgangspunktEntity entity = new AdgangspunktEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.adgangspunktRepository;
    }

    @Override
    public Collection<AdgangspunktVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public AdgangspunktVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdgangspunktVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdgangspunktVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdgangspunktVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdgangspunktVersionEntity createVersionEntity() {
        return new AdgangspunktVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HusnummerEntity husnummer;

    public HusnummerEntity getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }


}
