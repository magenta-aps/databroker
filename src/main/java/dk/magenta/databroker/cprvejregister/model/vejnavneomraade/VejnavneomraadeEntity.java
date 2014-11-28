package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneomraade")
public class VejnavneomraadeEntity
        extends DobbeltHistorikBase<VejnavneomraadeEntity, VejnavneomraadeVersionEntity>
        implements Serializable {


    protected VejnavneomraadeEntity() {
        this.versioner = new ArrayList<VejnavneomraadeVersionEntity>();
    }

    public static VejnavneomraadeEntity create() {
        VejnavneomraadeEntity entity = new VejnavneomraadeEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<VejnavneomraadeVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER)
    private VejnavneomraadeVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private VejnavneomraadeVersionEntity preferredVersion;

    @Override
    public Collection<VejnavneomraadeVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public VejnavneomraadeVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(VejnavneomraadeVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public VejnavneomraadeVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(VejnavneomraadeVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

    @Override
    protected VejnavneomraadeVersionEntity createVersionEntity() {
        return new VejnavneomraadeVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @ManyToOne(fetch = FetchType.LAZY)
    private IsoPunktEntity vejtilslutningspunkt;

    @OneToOne(mappedBy = "vejnavneomraade", fetch = FetchType.LAZY)
    private NavngivenVejEntity navngivenVej;

    public IsoPunktEntity getVejtilslutningspunkt() {
        return this.vejtilslutningspunkt;
    }

    public void setVejtilslutningspunkt(IsoPunktEntity vejtilslutningspunkt) {
        this.vejtilslutningspunkt = vejtilslutningspunkt;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }
}
