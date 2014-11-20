package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adresse", indexes = { @Index(name="husnummer", columnList="husnummer_id") })
public class AdresseEntity
        extends DobbeltHistorikEntity<AdresseEntity, AdresseRegistreringEntity, AdresseRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "doerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    private String doerbetegnelse;

    @Basic
    @Column(name = "etagebetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    private String etagebetegnelse;
    private HusnummerEntity husnummer;
    private DoerpunktEntity doerpunkt;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoerbetegnelse() {
        return this.doerbetegnelse;
    }

    public void setDoerbetegnelse(String doerbetegnelse) {
        this.doerbetegnelse = doerbetegnelse;
    }

    public String getEtagebetegnelse() {
        return this.etagebetegnelse;
    }

    public void setEtagebetegnelse(String etagebetegnelse) {
        this.etagebetegnelse = etagebetegnelse;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        AdresseEntity that = (AdresseEntity) other;

        if (this.getId() != that.getId()) {
            return false;
        }
        if (this.getUuid() != null ? !this.getUuid().equals(that.getUuid()) : that.getUuid() != null) {
            return false;
        }
        if (this.doerbetegnelse != null ? !this.doerbetegnelse.equals(that.doerbetegnelse) : that.doerbetegnelse != null) {
            return false;
        }
        if (this.etagebetegnelse != null ? !this.etagebetegnelse.equals(that.etagebetegnelse) : that.etagebetegnelse != null) {
            return false;
        }
        if (this.status != null ? !this.status.equals(that.status) : that.status != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
        result = 31 * result + (this.doerbetegnelse != null ? this.doerbetegnelse.hashCode() : 0);
        result = 31 * result + (this.etagebetegnelse != null ? this.etagebetegnelse.hashCode() : 0);
        return (int) result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husnummer_id", referencedColumnName = "husnummer_id", nullable = false)
    public HusnummerEntity getHusnummer() {
        return this.husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doerpunkt_id", referencedColumnName = "doerpunkt_id")
    public DoerpunktEntity getDoerpunkt() {
        return this.doerpunkt;
    }

    public void setDoerpunkt(DoerpunktEntity doerpunkt) {
        this.doerpunkt = doerpunkt;
    }
}
