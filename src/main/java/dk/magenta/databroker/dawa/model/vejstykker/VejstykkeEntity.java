package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_vejstykke")
public class VejstykkeEntity extends DobbeltHistorikBase<VejstykkeEntity, VejstykkeVersionEntity> {
    @OneToMany(mappedBy = "entity")
    private Collection<VejstykkeVersionEntity> versioner;

    @OneToOne
    private VejstykkeVersionEntity latestVersion;

    @OneToOne
    private VejstykkeVersionEntity preferredVersion;

    @Override
    public Collection<VejstykkeVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<VejstykkeVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public VejstykkeVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(VejstykkeVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public VejstykkeVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(VejstykkeVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected VejstykkeVersionEntity createVersionEntity() {
        return new VejstykkeVersionEntity(this);
    }

    /* Domain specific fields */

    @OneToMany(mappedBy = "vejstykke")
    private Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner;

    public Collection<AdgangsAdresseVersionEntity> getAdgangsAdresseVersioner() {
        return adgangsAdresseVersioner;
    }

    public void setAdgangsAdresseVersioner(Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner) {
        this.adgangsAdresseVersioner = adgangsAdresseVersioner;
    }
}
