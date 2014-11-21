package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "doerpunkt")
public class DoerpunktEntity
        extends DobbeltHistorikBase<DoerpunktEntity, DoerpunktRegistreringEntity, DoerpunktRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
    private String noejagtighedsklasse;

    @Basic
    @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
    private String kilde;

    @Basic
    @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
    private String tekniskStandard;

    @OneToMany(mappedBy = "doerpunkt")
    private Collection<AdresseEntity> adresser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private IsoPunktEntity position;


    public String getNoejagtighedsklasse() {
        return this.noejagtighedsklasse;
    }

    public void setNoejagtighedsklasse(String noejagtighedsklasse) {
        this.noejagtighedsklasse = noejagtighedsklasse;
    }

    public String getKilde() {
        return this.kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public String getTekniskStandard() {
        return this.tekniskStandard;
    }

    public void setTekniskStandard(String tekniskStandard) {
        this.tekniskStandard = tekniskStandard;
    }

    public Collection<AdresseEntity> getAdresser() {
        return this.adresser;
    }

    public void setAdresser(Collection<AdresseEntity> adresser) {
        this.adresser = adresser;
    }

    public IsoPunktEntity getPosition() {
        return this.position;
    }

    public void setPosition(IsoPunktEntity position) {
        this.position = position;
    }

    public static DoerpunktEntity create() {
        DoerpunktEntity entity = new DoerpunktEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.doerpunktRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }

        DoerpunktEntity that = (DoerpunktEntity) o;
        if (this.kilde != null ? !this.kilde.equals(that.kilde) : that.kilde != null) {
            return false;
        }
        if (this.noejagtighedsklasse != null ? !noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null) {
            return false;
        }
        if (this.tekniskStandard != null ? !tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.noejagtighedsklasse != null ? this.noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (this.kilde != null ? this.kilde.hashCode() : 0);
        result = 31 * result + (this.tekniskStandard != null ? this.tekniskStandard.hashCode() : 0);
        return (int) result;
    }

    @Override
    protected DoerpunktRegistreringEntity createRegistreringEntity() {
        return new DoerpunktRegistreringEntity(this);
    }
}
