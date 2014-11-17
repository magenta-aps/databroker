package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adgangspunkt")
public class AdgangspunktEntity {
    private int id;
    private String adgangspunktUuid;
    private String status;
    private String noejagtighedsklasse;
    private String kilde;
    private String tekniskStandard;
    private PostnummerEntity liggerIPostnummer;
    private ISOpointEntity position;
    private ISOpointEntity adgangspunktsretning;
    private ISOpointEntity husnummerretning;
    private Collection<HusnummerEntity> husnumre;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adgangspunkt_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "adgangspunkt_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getAdgangspunktUuid() {
        return adgangspunktUuid;
    }

    public void setAdgangspunktUuid(String adgangspunktUuid) {
        this.adgangspunktUuid = adgangspunktUuid;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "noejagtighedsklasse", nullable = false, insertable = true, updatable = true, length = 255)
    public String getNoejagtighedsklasse() {
        return noejagtighedsklasse;
    }

    public void setNoejagtighedsklasse(String noejagtighedsklasse) {
        this.noejagtighedsklasse = noejagtighedsklasse;
    }

    @Basic
    @Column(name = "kilde", nullable = false, insertable = true, updatable = true, length = 255)
    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    @Basic
    @Column(name = "teknisk_standard", nullable = false, insertable = true, updatable = true, length = 255)
    public String getTekniskStandard() {
        return tekniskStandard;
    }

    public void setTekniskStandard(String tekniskStandard) {
        this.tekniskStandard = tekniskStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdgangspunktEntity that = (AdgangspunktEntity) o;

        if (id != that.id) return false;
        if (adgangspunktUuid != null ? !adgangspunktUuid.equals(that.adgangspunktUuid) : that.adgangspunktUuid != null)
            return false;
        if (kilde != null ? !kilde.equals(that.kilde) : that.kilde != null) return false;
        if (noejagtighedsklasse != null ? !noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (tekniskStandard != null ? !tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (adgangspunktUuid != null ? adgangspunktUuid.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (noejagtighedsklasse != null ? noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (kilde != null ? kilde.hashCode() : 0);
        result = 31 * result + (tekniskStandard != null ? tekniskStandard.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligger_i_postnummer_id", referencedColumnName = "postnummer_id", nullable = false)
    public PostnummerEntity getLiggerIPostnummer() {
        return liggerIPostnummer;
    }

    public void setLiggerIPostnummer(PostnummerEntity liggerIPostnummer) {
        this.liggerIPostnummer = liggerIPostnummer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "iso_point_id", nullable = false)
    public ISOpointEntity getPosition() {
        return position;
    }

    public void setPosition(ISOpointEntity position) {
        this.position = position;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adgangspunktsretning_id", referencedColumnName = "iso_point_id", nullable = false)
    public ISOpointEntity getAdgangspunktsretning() {
        return adgangspunktsretning;
    }

    public void setAdgangspunktsretning(ISOpointEntity adgangspunktsretning) {
        this.adgangspunktsretning = adgangspunktsretning;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husnummerretning_id", referencedColumnName = "iso_point_id", nullable = false)
    public ISOpointEntity getHusnummerretning() {
        return husnummerretning;
    }

    public void setHusnummerretning(ISOpointEntity husnummerretning) {
        this.husnummerretning = husnummerretning;
    }

    @OneToMany(mappedBy = "tilknyttetAdgangspunkt")
    public Collection<HusnummerEntity> getHusnumre() {
        return husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
    }
}
