package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.stormodtagere.StormodtagerEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_adgangsadresse")
public class AdgangsAdresseEntity extends DobbeltHistorikBase<AdgangsAdresseEntity, AdgangsAdresseVersionEntity> {

    @OneToMany(mappedBy="entity")
    private Collection<AdgangsAdresseVersionEntity> versioner;

    @OneToOne
    private AdgangsAdresseVersionEntity latestVersion;

    @OneToOne
    private AdgangsAdresseVersionEntity preferredVersion;

    @Override
    public Collection<AdgangsAdresseVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<AdgangsAdresseVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public AdgangsAdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdgangsAdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdgangsAdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdgangsAdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdgangsAdresseVersionEntity createVersionEntity() {
        return new AdgangsAdresseVersionEntity(this);
    }

    /* Domain specific fields */
    @OneToOne(mappedBy = "adgangsadresse")
    private StormodtagerEntity stormodtager;

    @OneToMany(mappedBy = "adgangsadresse")
    private Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner;

    public StormodtagerEntity getStormodtager() {
        return stormodtager;
    }

    public void setStormodtager(StormodtagerEntity stormodtager) {
        this.stormodtager = stormodtager;
    }

    public Collection<EnhedsAdresseVersionEntity> getEnhedsAdresseVersioner() {
        return enhedsAdresseVersioner;
    }

    public void setEnhedsAdresseVersioner(Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner) {
        this.enhedsAdresseVersioner = enhedsAdresseVersioner;
    }
}
