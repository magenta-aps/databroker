package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneomraade")
public class VejnavneomraadeEntity {
    private int vejnavneomraadeId;
    private String vejnavneomraadeUuid;
    private String vejnavneomraade;
    private String vejnavnelinje;
    private String noejagtighedsklasse;
    private String kilde;
    private String tekniskStandard;
    private Collection<NavngivenVejEntity> navngivneVeje;
    private ISOpointEntity vejtilslutningspunkt;

    @Id
    @Column(name = "vejnavneomraade_id", nullable = false, insertable = true, updatable = true)
    public int getVejnavneomraadeId() {
        return vejnavneomraadeId;
    }

    public void setVejnavneomraadeId(int vejnavneomraadeId) {
        this.vejnavneomraadeId = vejnavneomraadeId;
    }

    @Basic
    @Column(name = "vejnavneomraade_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getVejnavneomraadeUuid() {
        return vejnavneomraadeUuid;
    }

    public void setVejnavneomraadeUuid(String vejnavneomraadeUuid) {
        this.vejnavneomraadeUuid = vejnavneomraadeUuid;
    }

    @Basic
    @Column(name = "vejnavneomraade", nullable = false, insertable = true, updatable = true, columnDefinition="Text")
    public String getVejnavneomraade() {
        return vejnavneomraade;
    }

    public void setVejnavneomraade(String vejnavneomraade) {
        this.vejnavneomraade = vejnavneomraade;
    }

    @Basic
    @Column(name = "vejnavnelinje", nullable = true, insertable = true, updatable = true, length = 255)
    public String getVejnavnelinje() {
        return vejnavnelinje;
    }

    public void setVejnavnelinje(String vejnavnelinje) {
        this.vejnavnelinje = vejnavnelinje;
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

        VejnavneomraadeEntity that = (VejnavneomraadeEntity) o;

        if (vejnavneomraadeId != that.vejnavneomraadeId) return false;
        if (kilde != null ? !kilde.equals(that.kilde) : that.kilde != null) return false;
        if (noejagtighedsklasse != null ? !noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null)
            return false;
        if (tekniskStandard != null ? !tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null)
            return false;
        if (vejnavnelinje != null ? !vejnavnelinje.equals(that.vejnavnelinje) : that.vejnavnelinje != null)
            return false;
        if (vejnavneomraade != null ? !vejnavneomraade.equals(that.vejnavneomraade) : that.vejnavneomraade != null)
            return false;
        if (vejnavneomraadeUuid != null ? !vejnavneomraadeUuid.equals(that.vejnavneomraadeUuid) : that.vejnavneomraadeUuid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vejnavneomraadeId;
        result = 31 * result + (vejnavneomraadeUuid != null ? vejnavneomraadeUuid.hashCode() : 0);
        result = 31 * result + (vejnavneomraade != null ? vejnavneomraade.hashCode() : 0);
        result = 31 * result + (vejnavnelinje != null ? vejnavnelinje.hashCode() : 0);
        result = 31 * result + (noejagtighedsklasse != null ? noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (kilde != null ? kilde.hashCode() : 0);
        result = 31 * result + (tekniskStandard != null ? tekniskStandard.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "vejnavneomraade")
    public Collection<NavngivenVejEntity> getNavngivneVeje() {
        return navngivneVeje;
    }

    public void setNavngivneVeje(Collection<NavngivenVejEntity> navngivneVeje) {
        this.navngivneVeje = navngivneVeje;
    }

    @ManyToOne
    @JoinColumn(name = "vejtilslutningspunkt_id", referencedColumnName = "iso_point_id", nullable = false)
    public ISOpointEntity getVejtilslutningspunkt() {
        return vejtilslutningspunkt;
    }

    public void setVejtilslutningspunkt(ISOpointEntity vejtilslutningspunkt) {
        this.vejtilslutningspunkt = vejtilslutningspunkt;
    }
}
