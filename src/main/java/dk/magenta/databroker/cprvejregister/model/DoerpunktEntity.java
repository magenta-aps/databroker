package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "doerpunkt")
public class DoerpunktEntity {
    private int id;
    private String doerpunktUuid;
    private String noejagtighedsklasse;
    private String kilde;
    private String tekniskStandard;
    private Collection<AdresseEntity> adresser;
    private ISOpointEntity position;

    @Id
    @Column(name = "doerpunkt_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "doerpunkt_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getDoerpunktUuid() {
        return doerpunktUuid;
    }

    public void setDoerpunktUuid(String doerpunktUuid) {
        this.doerpunktUuid = doerpunktUuid;
    }

    @Basic
    @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
    public String getNoejagtighedsklasse() {
        return noejagtighedsklasse;
    }

    public void setNoejagtighedsklasse(String noejagtighedsklasse) {
        this.noejagtighedsklasse = noejagtighedsklasse;
    }

    @Basic
    @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    @Basic
    @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
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

        DoerpunktEntity that = (DoerpunktEntity) o;

        if (id != that.id) return false;
        if (doerpunktUuid != null ? !doerpunktUuid.equals(that.doerpunktUuid) : that.doerpunktUuid != null)
            return false;
        if (kilde != null ? !kilde.equals(that.kilde) : that.kilde != null) return false;
        if (noejagtighedsklasse != null ? !noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null)
            return false;
        if (tekniskStandard != null ? !tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (doerpunktUuid != null ? doerpunktUuid.hashCode() : 0);
        result = 31 * result + (noejagtighedsklasse != null ? noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (kilde != null ? kilde.hashCode() : 0);
        result = 31 * result + (tekniskStandard != null ? tekniskStandard.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "doerpunkt")
    public Collection<AdresseEntity> getAdresser() {
        return adresser;
    }

    public void setAdresser(Collection<AdresseEntity> adresser) {
        this.adresser = adresser;
    }

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "iso_point_id", nullable = false)
    public ISOpointEntity getPosition() {
        return position;
    }

    public void setPosition(ISOpointEntity position) {
        this.position = position;
    }
}
