package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "reserveret_vejnavn")
public class ReserveretVejnavnEntity
        extends DobbeltHistorikBase<ReserveretVejnavnEntity, ReserveretVejnavnVersionEntity>
        implements Serializable {

    protected ReserveretVejnavnEntity() {
        this.versioner = new ArrayList<ReserveretVejnavnVersionEntity>();
    }

    public static ReserveretVejnavnEntity create() {
        ReserveretVejnavnEntity entity = new ReserveretVejnavnEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ReserveretVejnavnVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER)
    private ReserveretVejnavnVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private ReserveretVejnavnVersionEntity preferredVersion;

    @Override
    public Collection<ReserveretVejnavnVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public ReserveretVejnavnVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(ReserveretVejnavnVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public ReserveretVejnavnVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(ReserveretVejnavnVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

    @Override
    protected ReserveretVejnavnVersionEntity createVersionEntity() {
        return new ReserveretVejnavnVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CprKommuneEntity reserveretAfKommune;

    public CprKommuneEntity getReserveretAfKommune() {
        return this.reserveretAfKommune;
    }

    public void setReserveretAfKommune(CprKommuneEntity reserveretAfKommune) {
        this.reserveretAfKommune = reserveretAfKommune;
    }

    public String getTypeName() {
        return "reserveretVejnavn";
    }
    public JSONObject toJSON() {
        return new JSONObject();
    }

}
