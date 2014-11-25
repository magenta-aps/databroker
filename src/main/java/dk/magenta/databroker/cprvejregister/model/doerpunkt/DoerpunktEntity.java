package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "doerpunkt")
public class DoerpunktEntity
        extends DobbeltHistorikBase<DoerpunktEntity, DoerpunktVersionEntity>
        implements Serializable {

    @OneToMany
    private Collection<DoerpunktVersionEntity> versions;

    @OneToOne
    private DoerpunktVersionEntity latestVersion;

    @OneToOne
    private DoerpunktVersionEntity preferredVersion;




    protected DoerpunktEntity() {
        this.versions = new ArrayList<DoerpunktVersionEntity>();
    }

    public static DoerpunktEntity create() {
        DoerpunktEntity entity = new DoerpunktEntity();
        entity.generateNewUUID();
        return entity;
    }


    @Override
    public Collection<DoerpunktVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public DoerpunktVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(DoerpunktVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public DoerpunktVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(DoerpunktVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected DoerpunktVersionEntity createVersionEntity() {
        return new DoerpunktVersionEntity(this);
    }

    /*
    * Fields on the entity
    * */

    @OneToOne(mappedBy = "doerPunkt", fetch = FetchType.LAZY, optional = false)
    private AdresseEntity adresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private IsoPunktEntity position;


    public AdresseEntity getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseEntity adresse) {
        this.adresse = adresse;
    }

    public IsoPunktEntity getPosition() {
        return this.position;
    }

    public void setPosition(IsoPunktEntity position) {
        this.position = position;
    }


}
