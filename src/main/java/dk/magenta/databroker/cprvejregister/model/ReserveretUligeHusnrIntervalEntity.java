package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "reserveret_ulige_husnr_interval")
public class ReserveretUligeHusnrIntervalEntity {
    private int id;
    private Integer intervalStart;
    private Integer intervalSlut;
    private String notat;
    private KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVej;

    @Id
    @Column(name = "reserveret_ulige_husnr_interval_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "interval_start", nullable = true, insertable = true, updatable = true)
    public Integer getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(Integer intervalStart) {
        this.intervalStart = intervalStart;
    }

    @Basic
    @Column(name = "interval_slut", nullable = true, insertable = true, updatable = true)
    public Integer getIntervalSlut() {
        return intervalSlut;
    }

    public void setIntervalSlut(Integer intervalSlut) {
        this.intervalSlut = intervalSlut;
    }

    @Basic
    @Column(name = "notat", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    public String getNotat() {
        return notat;
    }

    public void setNotat(String notat) {
        this.notat = notat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReserveretUligeHusnrIntervalEntity that = (ReserveretUligeHusnrIntervalEntity) o;

        if (id != that.id) return false;
        if (intervalSlut != null ? !intervalSlut.equals(that.intervalSlut) : that.intervalSlut != null) return false;
        if (intervalStart != null ? !intervalStart.equals(that.intervalStart) : that.intervalStart != null)
            return false;
        if (notat != null ? !notat.equals(that.notat) : that.notat != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (intervalStart != null ? intervalStart.hashCode() : 0);
        result = 31 * result + (intervalSlut != null ? intervalSlut.hashCode() : 0);
        result = 31 * result + (notat != null ? notat.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(
            name = "kommunedel_af_navngiven_vej_id",
            referencedColumnName = "kommunedel_af_navngiven_vej_id",
            nullable = false
    )
    public KommunedelAfNavngivenVejEntity getKommunedelAfNavngivenVej() {
        return kommunedelAfNavngivenVej;
    }

    public void setKommunedelAfNavngivenVej(KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVej) {
        this.kommunedelAfNavngivenVej = kommunedelAfNavngivenVej;
    }
}
