package dk.magenta.databroker.dawa.model.ejerlav;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_ejerlav")
public class EjerLavEntity extends DobbeltHistorikBase<EjerLavEntity, EjerLavVersionEntity> {
    @OneToMany(mappedBy="entity")
    private Collection<EjerLavVersionEntity> versioner;

    @OneToOne
    private EjerLavVersionEntity latestVersion;

    @OneToOne
    private EjerLavVersionEntity preferredVersion;

    public Collection<EjerLavVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<EjerLavVersionEntity> versioner) {
        this.versioner = versioner;
    }

    public EjerLavVersionEntity getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(EjerLavVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    public EjerLavVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    public void setPreferredVersion(EjerLavVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected EjerLavVersionEntity createVersionEntity() {
        return new EjerLavVersionEntity(this);
    }

    /* Domain specific fields */
    @OneToMany(mappedBy = "ejerlav")
    private Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner;

    public Collection<AdgangsAdresseVersionEntity> getAdgangsAdresseVersioner() {
        return adgangsAdresseVersioner;
    }

    public void setAdgangsAdresseVersioner(Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner) {
        this.adgangsAdresseVersioner = adgangsAdresseVersioner;
    }
}
