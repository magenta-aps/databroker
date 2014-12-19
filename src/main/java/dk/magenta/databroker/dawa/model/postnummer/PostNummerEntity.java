package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_postnummer")
public class PostNummerEntity extends DobbeltHistorikBase<PostNummerEntity, PostNummerVersionEntity> {

    @OneToMany(mappedBy="entity")
    private Collection<PostNummerVersionEntity> versioner;

    @OneToOne
    private PostNummerVersionEntity latestVersion;

    @OneToOne
    private PostNummerVersionEntity preferredVersion;

    @Override
    public Collection<PostNummerVersionEntity> getVersioner() {
        return versioner;
    }

    protected void setVersioner(Collection<PostNummerVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public PostNummerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(PostNummerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public PostNummerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(PostNummerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected PostNummerVersionEntity createVersionEntity() {
        return new PostNummerVersionEntity(this);
    }

    /* Domain specific fields */
    @OneToMany(mappedBy = "postnummer")
    private Collection<VejstykkeVersionEntity> vejstykkeVersioner;

    public Collection<VejstykkeVersionEntity> getVejstykkeVersioner() {
        return vejstykkeVersioner;
    }

    public void setVejstykkeVersioner(Collection<VejstykkeVersionEntity> vejstykkeVersioner) {
        this.vejstykkeVersioner = vejstykkeVersioner;
    }
}
