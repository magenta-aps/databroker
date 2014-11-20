package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adgangspunkt")
public class AdgangspunktEntity
        extends DobbeltHistorikEntity<AdgangspunktEntity, AdgangspunktRegistreringEntity, AdgangspunktRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "noejagtighedsklasse", nullable = false, insertable = true, updatable = true, length = 255)
    private String noejagtighedsklasse;

    @Basic
    @Column(name = "kilde", nullable = false, insertable = true, updatable = true, length = 255)
    private String kilde;

    @Basic
    @Column(name = "teknisk_standard", nullable = false, insertable = true, updatable = true, length = 255)
    private String tekniskStandard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligger_i_postnummer_id", nullable = false)
    private PostnummerEntity liggerIPostnummer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private IsoPunktEntity position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adgangspunktsretning_id", nullable = false)
    private IsoPunktEntity adgangspunktsretning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husnummerretning_id", nullable = false)
    private IsoPunktEntity husnummerretning;

    @OneToMany(mappedBy = "tilknyttetAdgangspunkt")
    private Collection<HusnummerEntity> husnumre;


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public PostnummerEntity getLiggerIPostnummer() {
        return this.liggerIPostnummer;
    }

    public void setLiggerIPostnummer(PostnummerEntity liggerIPostnummer) {
        this.liggerIPostnummer = liggerIPostnummer;
    }

    public IsoPunktEntity getPosition() {
        return this.position;
    }

    public void setPosition(IsoPunktEntity position) {
        this.position = position;
    }

    public IsoPunktEntity getAdgangspunktsretning() {
        return this.adgangspunktsretning;
    }

    public void setAdgangspunktsretning(IsoPunktEntity adgangspunktsretning) {
        this.adgangspunktsretning = adgangspunktsretning;
    }

    public IsoPunktEntity getHusnummerretning() {
        return this.husnummerretning;
    }

    public void setHusnummerretning(IsoPunktEntity husnummerretning) {
        this.husnummerretning = husnummerretning;
    }

    public Collection<HusnummerEntity> getHusnumre() {
        return this.husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }

        AdgangspunktEntity that = (AdgangspunktEntity) o;

        if (this.kilde != null ? !this.kilde.equals(that.kilde) : that.kilde != null) {
            return false;
        }
        if (this.noejagtighedsklasse != null ? !this.noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null) {
            return false;
        }
        if (this.status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (this.tekniskStandard != null ? !this.tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
        result = 31 * result + (this.noejagtighedsklasse != null ? this.noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (this.kilde != null ? this.kilde.hashCode() : 0);
        result = 31 * result + (this.tekniskStandard != null ? this.tekniskStandard.hashCode() : 0);
        return (int) result;
    }

}
