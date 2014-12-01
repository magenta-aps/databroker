package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.hibernate.annotations.Index;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adresse")
public class AdresseEntity
        extends DobbeltHistorikBase<AdresseEntity, AdresseVersionEntity>
        implements Serializable {

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
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

    @Override
    public Collection<AdresseVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public AdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdresseVersionEntity createVersionEntity() {
        return new AdresseVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private HusnummerEntity husnummer;

    @OneToOne(fetch = FetchType.LAZY)
    private DoerpunktEntity doerPunkt;

    public HusnummerEntity getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }

    public DoerpunktEntity getDoerPunkt() {
        return doerPunkt;
    }

    public void setDoerPunkt(DoerpunktEntity doerPunkt) {
        this.doerPunkt = doerPunkt;
    }
}
