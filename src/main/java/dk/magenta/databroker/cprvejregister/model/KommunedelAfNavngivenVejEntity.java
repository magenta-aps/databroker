package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommunedel_af_navngiven_vej")
public class KommunedelAfNavngivenVejEntity {
    private int id;
    private int vejkode;
    private NavngivenVejEntity navngivenVej;
    private KommuneEntity kommune;
    private Collection<ReserveretLigeHusnrIntervalEntity> reserveredeLigeHusnrIntervalller;
    private Collection<ReserveretUligeHusnrIntervalEntity> reserveredeUligeHusnrIntervaller;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "kommunedel_af_navngiven_vej_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "vejkode", nullable = false, insertable = true, updatable = true)
    public int getVejkode() {
        return vejkode;
    }

    public void setVejkode(int vejkode) {
        this.vejkode = vejkode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KommunedelAfNavngivenVejEntity that = (KommunedelAfNavngivenVejEntity) o;

        if (id != that.id) return false;
        if (vejkode != that.vejkode) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + vejkode;
        return result;
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
    @JoinColumn(name = "kommune_id", referencedColumnName = "kommune_id", nullable = false)
    public KommuneEntity getKommune() {
        return kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }

    @OneToMany(mappedBy = "kommunedelAfNavngivenVej")
    public Collection<ReserveretLigeHusnrIntervalEntity> getReserveredeLigeHusnrIntervalller() {
        return reserveredeLigeHusnrIntervalller;
    }

    public void setReserveredeLigeHusnrIntervalller(Collection<ReserveretLigeHusnrIntervalEntity> reserveredeLigeHusnrIntervalller) {
        this.reserveredeLigeHusnrIntervalller = reserveredeLigeHusnrIntervalller;
    }

    @OneToMany(mappedBy = "kommunedelAfNavngivenVej")
    public Collection<ReserveretUligeHusnrIntervalEntity> getReserveredeUligeHusnrIntervaller() {
        return reserveredeUligeHusnrIntervaller;
    }

    public void setReserveredeUligeHusnrIntervaller(Collection<ReserveretUligeHusnrIntervalEntity> reserveredeUligeHusnrIntervaller) {
        this.reserveredeUligeHusnrIntervaller = reserveredeUligeHusnrIntervaller;
    }
}
