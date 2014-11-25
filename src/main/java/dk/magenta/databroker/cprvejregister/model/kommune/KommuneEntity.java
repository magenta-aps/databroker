package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.reserveretvejnavn.ReserveretVejnavnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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

    @Basic
    @Column(name = "kommunekode", nullable = false, insertable = true, updatable = true, unique = true)
    private int kommunekode;

    @OneToMany(mappedBy = "kommune")
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

    @OneToMany(mappedBy = "ansvarligKommune")
    private Collection<NavngivenVejEntity> ansvarligForNavngivneVeje;

    @OneToMany(mappedBy = "reserveretAfKommune")
    private Collection<ReserveretVejnavnEntity> reserveredeVejnavne;




    @OneToMany(mappedBy = "entitet")
    private Collection<KommuneVersionEntity> versioner;

    @OneToOne
    private KommuneVersionEntity latestVersion;

    @OneToOne
    private KommuneVersionEntity preferredVersion;

    protected KommuneEntity() {
        this.versioner = new ArrayList<KommuneVersionEntity>();
    }




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

    public Collection<NavngivenVejEntity> getAnsvarligForNavngivneVeje() {
        return this.ansvarligForNavngivneVeje;
    }

    public void setAnsvarligForNavngivneVeje(Collection<NavngivenVejEntity> ansvarligForNavngivneVeje) {
        this.ansvarligForNavngivneVeje = ansvarligForNavngivneVeje;
    }

    public Collection<ReserveretVejnavnEntity> getReserveredeVejnavne() {
        return this.reserveredeVejnavne;
    }

    public void setReserveredeVejnavne(Collection<ReserveretVejnavnEntity> reserveredeVejnavne) {
        this.reserveredeVejnavne = reserveredeVejnavne;
    }

    public static KommuneEntity create() {
        KommuneEntity entity = new KommuneEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.kommuneRepository;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }

        KommuneEntity that = (KommuneEntity) other;

        if (this.kommunekode != that.kommunekode) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + this.kommunekode;
        return (int) result;
    }


    @Override
    public Collection<KommuneVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public KommuneVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(KommuneVersionEntity newLatest) {
        this.latestVersion = newLatest;
    }

    @Override
    public KommuneVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(KommuneVersionEntity newPreferred) {
        this.preferredVersion = newPreferred;
    }

    @Override
    protected KommuneVersionEntity createVersionEntity() {
        return new KommuneVersionEntity(this);
    }
}
