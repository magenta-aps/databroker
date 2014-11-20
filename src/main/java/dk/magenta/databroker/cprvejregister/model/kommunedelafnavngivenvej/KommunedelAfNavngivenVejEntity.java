package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.ReserveretLigeHusnrIntervalEntity;
import dk.magenta.databroker.cprvejregister.model.ReserveretUligeHusnrIntervalEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommunedel_af_navngiven_vej", indexes = { @Index(name="kommune", columnList="kommune_id"), @Index(name="navngivenVej", columnList="navngiven_vej_id") })
public class KommunedelAfNavngivenVejEntity
        extends DobbeltHistorikEntity<KommunedelAfNavngivenVejEntity, KommunedelAfNavngivenVejRegistreringEntity, KommunedelAfNavngivenVejRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "vejkode", nullable = false, insertable = true, updatable = true)
    private int vejkode;

    @ManyToOne
    @JoinColumn(name = "navngiven_vej_id", referencedColumnName = "navngiven_vej_id", nullable = false)
    private NavngivenVejEntity navngivenVej;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kommune_id", referencedColumnName = "kommune_id", nullable = false)
    private KommuneEntity kommune;

    @OneToMany(mappedBy = "kommunedelAfNavngivenVej")
    private Collection<ReserveretLigeHusnrIntervalEntity> reserveredeLigeHusnrIntervalller;

    @OneToMany(mappedBy = "kommunedelAfNavngivenVej")
    private Collection<ReserveretUligeHusnrIntervalEntity> reserveredeUligeHusnrIntervaller;


    public int getVejkode() {
        return this.vejkode;
    }

    public void setVejkode(int vejkode) {
        this.vejkode = vejkode;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }

    public KommuneEntity getKommune() {
        return this.kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }

    public Collection<ReserveretLigeHusnrIntervalEntity> getReserveredeLigeHusnrIntervalller() {
        return this.reserveredeLigeHusnrIntervalller;
    }

    public void setReserveredeLigeHusnrIntervalller(Collection<ReserveretLigeHusnrIntervalEntity> reserveredeLigeHusnrIntervalller) {
        this.reserveredeLigeHusnrIntervalller = reserveredeLigeHusnrIntervalller;
    }

    public Collection<ReserveretUligeHusnrIntervalEntity> getReserveredeUligeHusnrIntervaller() {
        return this.reserveredeUligeHusnrIntervaller;
    }

    public void setReserveredeUligeHusnrIntervaller(Collection<ReserveretUligeHusnrIntervalEntity> reserveredeUligeHusnrIntervaller) {
        this.reserveredeUligeHusnrIntervaller = reserveredeUligeHusnrIntervaller;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }

        KommunedelAfNavngivenVejEntity that = (KommunedelAfNavngivenVejEntity) other;

        if (this.vejkode != that.vejkode) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long result = getId();
        result = 31 * result + this.vejkode;
        return (int) result;
    }

}
