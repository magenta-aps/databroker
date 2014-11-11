package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "Husnummer")
public class HusnummerEntity {
    private int id;
    private String husnummerUuid;
    private String husnummerbetegnelse;
    private Collection<AdresseEntity> adresser;
    private NavngivenVejEntity navngivenVej;
    private AdgangspunktEntity tilknyttetAdgangspunkt;

    @Id
    @Column(name = "husnummer_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "husnummer_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getHusnummerUuid() {
        return husnummerUuid;
    }

    public void setHusnummerUuid(String husnummerUuid) {
        this.husnummerUuid = husnummerUuid;
    }

    @Basic
    @Column(name = "husnummerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    public String getHusnummerbetegnelse() {
        return husnummerbetegnelse;
    }

    public void setHusnummerbetegnelse(String husnummerbetegnelse) {
        this.husnummerbetegnelse = husnummerbetegnelse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HusnummerEntity that = (HusnummerEntity) o;

        if (id != that.id) return false;
        if (husnummerUuid != null ? !husnummerUuid.equals(that.husnummerUuid) : that.husnummerUuid != null)
            return false;
        if (husnummerbetegnelse != null ? !husnummerbetegnelse.equals(that.husnummerbetegnelse) : that.husnummerbetegnelse != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (husnummerUuid != null ? husnummerUuid.hashCode() : 0);
        result = 31 * result + (husnummerbetegnelse != null ? husnummerbetegnelse.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "husnummer")
    public Collection<AdresseEntity> getAdresser() {
        return adresser;
    }

    public void setAdresser(Collection<AdresseEntity> adresser) {
        this.adresser = adresser;
    }

    @ManyToOne
    @JoinColumn(name = "navngiven_vej_id", referencedColumnName = "navngiven_vej_id", nullable = false)
    public NavngivenVejEntity getNavngivenVej() {
        return navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }

    @ManyToOne
    @JoinColumn(name = "tilknyttet_adgangspunkt_id", referencedColumnName = "adgangspunkt_id", nullable = false)
    public AdgangspunktEntity getTilknyttetAdgangspunkt() {
        return tilknyttetAdgangspunkt;
    }

    public void setTilknyttetAdgangspunkt(AdgangspunktEntity tilknyttetAdgangspunkt) {
        this.tilknyttetAdgangspunkt = tilknyttetAdgangspunkt;
    }
}
