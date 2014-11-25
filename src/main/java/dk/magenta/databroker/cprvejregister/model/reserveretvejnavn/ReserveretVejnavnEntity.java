package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
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

    @OneToMany(mappedBy = "entity")
    private Collection<ReserveretVejnavnVersionEntity> versioner;

    @OneToOne
    private ReserveretVejnavnVersionEntity latestVersion;

    @OneToOne
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserveret_af_kommune_id", nullable = false)
    private KommuneEntity reserveretAfKommune;

    public KommuneEntity getReserveretAfKommune() {
        return this.reserveretAfKommune;
    }

    public void setReserveretAfKommune(KommuneEntity reserveretAfKommune) {
        this.reserveretAfKommune = reserveretAfKommune;
    }

}
