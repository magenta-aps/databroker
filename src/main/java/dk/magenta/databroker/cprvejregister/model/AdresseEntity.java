package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adresse", indexes = { @Index(name="husnummer", columnList="husnummer_id") })
public class AdresseEntity {
    private int id;
    private String adresseUuid;
    private String status;
    private String doerbetegnelse;
    private String etagebetegnelse;
    private HusnummerEntity husnummer;
    private DoerpunktEntity doerpunkt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adresse_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "adresse_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getAdresseUuid() {
        return adresseUuid;
    }

    public void setAdresseUuid(String adresseUuid) {
        this.adresseUuid = adresseUuid;
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
    @Column(name = "doerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    public String getDoerbetegnelse() {
        return doerbetegnelse;
    }

    public void setDoerbetegnelse(String doerbetegnelse) {
        this.doerbetegnelse = doerbetegnelse;
    }

    @Basic
    @Column(name = "etagebetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
    public String getEtagebetegnelse() {
        return etagebetegnelse;
    }

    public void setEtagebetegnelse(String etagebetegnelse) {
        this.etagebetegnelse = etagebetegnelse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdresseEntity that = (AdresseEntity) o;

        if (id != that.id) return false;
        if (adresseUuid != null ? !adresseUuid.equals(that.adresseUuid) : that.adresseUuid != null) return false;
        if (doerbetegnelse != null ? !doerbetegnelse.equals(that.doerbetegnelse) : that.doerbetegnelse != null)
            return false;
        if (etagebetegnelse != null ? !etagebetegnelse.equals(that.etagebetegnelse) : that.etagebetegnelse != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (adresseUuid != null ? adresseUuid.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (doerbetegnelse != null ? doerbetegnelse.hashCode() : 0);
        result = 31 * result + (etagebetegnelse != null ? etagebetegnelse.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husnummer_id", referencedColumnName = "husnummer_id", nullable = false)
    public HusnummerEntity getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doerpunkt_id", referencedColumnName = "doerpunkt_id")
    public DoerpunktEntity getDoerpunkt() {
        return doerpunkt;
    }

    public void setDoerpunkt(DoerpunktEntity doerpunkt) {
        this.doerpunkt = doerpunkt;
    }
}
