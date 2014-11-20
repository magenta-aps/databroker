package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "husnummer", indexes = { @Index(name="navngivenVej", columnList="navngiven_vej_id"), @Index(name="adgangspunkt", columnList="tilknyttet_adgangspunkt_id") })
public class HusnummerEntity
        extends DobbeltHistorikBase<HusnummerEntity, HusnummerRegistreringEntity, HusnummerRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "husnummerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    private String husnummerbetegnelse;

    @OneToMany(mappedBy = "husnummer")
    private Collection<AdresseEntity> adresser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navngiven_vej_id", nullable = false)
    private NavngivenVejEntity navngivenVej;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tilknyttet_adgangspunkt_id", nullable = true)
    private AdgangspunktEntity tilknyttetAdgangspunkt;


    public String getHusnummerbetegnelse() {
        return this.husnummerbetegnelse;
    }

    public void setHusnummerbetegnelse(String husnummerbetegnelse) {
        this.husnummerbetegnelse = husnummerbetegnelse;
    }

    public Collection<AdresseEntity> getAdresser() {
        return this.adresser;
    }

    public void setAdresser(Collection<AdresseEntity> adresser) {
        this.adresser = adresser;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }

    public AdgangspunktEntity getTilknyttetAdgangspunkt() {
        return this.tilknyttetAdgangspunkt;
    }

    public void setTilknyttetAdgangspunkt(AdgangspunktEntity tilknyttetAdgangspunkt) {
        this.tilknyttetAdgangspunkt = tilknyttetAdgangspunkt;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }

        HusnummerEntity that = (HusnummerEntity) other;

        if (this.husnummerbetegnelse != null ? !this.husnummerbetegnelse.equals(that.husnummerbetegnelse) : that.husnummerbetegnelse != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.husnummerbetegnelse != null ? this.husnummerbetegnelse.hashCode() : 0);
        return (int) result;
    }

}
