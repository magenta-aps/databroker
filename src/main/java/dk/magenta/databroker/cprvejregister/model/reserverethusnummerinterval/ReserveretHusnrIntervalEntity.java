package dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "reserveret_husnr_interval")
public class ReserveretHusnrIntervalEntity extends UniqueBase implements Serializable {

    public ReserveretHusnrIntervalEntity() {

    }

    public static ReserveretHusnrIntervalEntity create() {
        return new ReserveretHusnrIntervalEntity();
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Column(name = "interval_start", nullable = true, insertable = true, updatable = true)
    private Integer intervalStart;

    @Basic
    @Column(name = "interval_slut", nullable = true, insertable = true, updatable = true)
    private Integer intervalSlut;

    @Basic
    @Column(name = "lige", nullable = false, insertable = true, updatable = false)
    private boolean lige;

    @Basic
    @Column(name = "notat", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    private String notat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVej;

    public Integer getIntervalStart() {
        return this.intervalStart;
    }

    public void setIntervalStart(Integer intervalStart) {
        this.intervalStart = intervalStart;
    }

    public Integer getIntervalSlut() {
        return this.intervalSlut;
    }

    public void setIntervalSlut(Integer intervalSlut) {
        this.intervalSlut = intervalSlut;
    }

    public Boolean isLige() {
        return lige;
    }

    public void setLige(boolean lige) {
        this.lige = lige;
    }

    public String getNotat() {
        return this.notat;
    }

    public void setNotat(String notat) {
        this.notat = notat;
    }

    public KommunedelAfNavngivenVejEntity getKommunedelAfNavngivenVej() {
        return this.kommunedelAfNavngivenVej;
    }

    public void setKommunedelAfNavngivenVej(KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVej) {
        this.kommunedelAfNavngivenVej = kommunedelAfNavngivenVej;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }
        ReserveretHusnrIntervalEntity that = (ReserveretHusnrIntervalEntity) o;
        if (this.intervalSlut != null ? !this.intervalSlut.equals(that.intervalSlut) : that.intervalSlut != null) {
            return false;
        }
        if (this.intervalStart != null ? !this.intervalStart.equals(that.intervalStart) : that.intervalStart != null) {
            return false;
        }
        if (this.lige != that.lige) {
            return false;
        }
        if (this.notat != null ? !this.notat.equals(that.notat) : that.notat != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.intervalStart != null ? this.intervalStart.hashCode() : 0);
        result = 31 * result + (this.intervalSlut != null ? this.intervalSlut.hashCode() : 0);
        result = 31 * result + (this.lige ? 1 : 0);
        result = 31 * result + (this.notat != null ? this.notat.hashCode() : 0);
        return (int) result;
    }



    public String getTypeName() {
        return "reserveretHusnrInterval";
    }
    public JSONObject toJSON() {
        return new JSONObject();
    }

}
