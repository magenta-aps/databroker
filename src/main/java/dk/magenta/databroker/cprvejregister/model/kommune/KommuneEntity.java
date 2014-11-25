package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.reserveretvejnavn.ReserveretVejnavnEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommune", indexes = { @Index(name = "kommunekode", columnList = "kommunekode") })
public class KommuneEntity
        extends DobbeltHistorikBase<KommuneEntity, KommuneVersionEntity>
        implements Serializable {


    protected KommuneEntity() {
        this.versioner = new ArrayList<KommuneVersionEntity>();
    }

    public static KommuneEntity create() {
        KommuneEntity entity = new KommuneEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
    private Collection<KommuneVersionEntity> versioner;

    @OneToOne(fetch = FetchType.LAZY)
    private KommuneVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private KommuneVersionEntity preferredVersion;


    @Override
    public Collection<KommuneVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public KommuneVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(KommuneVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public KommuneVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(KommuneVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

    @Override
    protected KommuneVersionEntity createVersionEntity() {
        return new KommuneVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Column(name = "kommunekode", nullable = false, insertable = true, updatable = true, unique = true)
    private int kommunekode;

    @OneToMany(mappedBy = "kommune", fetch = FetchType.LAZY)
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

    @OneToMany(mappedBy = "ansvarligKommune", fetch = FetchType.LAZY)
    private Collection<NavngivenVejVersionEntity> ansvarligForNavngivneVeje;

    @OneToMany(mappedBy = "reserveretAfKommune", fetch = FetchType.LAZY)
    private Collection<ReserveretVejnavnEntity> reserveredeVejnavne;

    public int getKommunekode() {
        return this.kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.kommunekode = kommunekode;
    }

    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return this.kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    public Collection<NavngivenVejVersionEntity> getAnsvarligForNavngivneVeje() {
        return ansvarligForNavngivneVeje;
    }

    public void setAnsvarligForNavngivneVeje(Collection<NavngivenVejVersionEntity> ansvarligForNavngivneVeje) {
        this.ansvarligForNavngivneVeje = ansvarligForNavngivneVeje;
    }

    public Collection<ReserveretVejnavnEntity> getReserveredeVejnavne() {
        return this.reserveredeVejnavne;
    }

    public void setReserveredeVejnavne(Collection<ReserveretVejnavnEntity> reserveredeVejnavne) {
        this.reserveredeVejnavne = reserveredeVejnavne;
    }

}
