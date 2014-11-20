package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "reserveret_vejnavn")
public class ReserveretVejnavnEntity {
    private int id;
    private String reserveretVejnavnUuid;
    private String navneomraade;
    private String reserveretNavn;
    private String status;
    private Date reservationsudloebsdato;
    private String retskrivningskontrol;
    private KommuneEntity reserveretAfKommune;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reserveret_vejnavn_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "reserveret_vejnavn_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getReserveretVejnavnUuid() {
        return reserveretVejnavnUuid;
    }

    public void setReserveretVejnavnUuid(String reserveretVejnavnUuid) {
        this.reserveretVejnavnUuid = reserveretVejnavnUuid;
    }

    @Basic
    @Column(name = "navneomraade", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    public String getNavneomraade() {
        return navneomraade;
    }

    public void setNavneomraade(String navneomraade) {
        this.navneomraade = navneomraade;
    }

    @Basic
    @Column(name = "reserveret_navn", nullable = true, insertable = true, updatable = true, length = 255)
    public String getReserveretNavn() {
        return reserveretNavn;
    }

    public void setReserveretNavn(String reserveretNavn) {
        this.reserveretNavn = reserveretNavn;
    }

    @Basic
    @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "reservationsudloebsdato", nullable = true, insertable = true, updatable = true)
    public Date getReservationsudloebsdato() {
        return reservationsudloebsdato;
    }

    public void setReservationsudloebsdato(Date reservationsudloebsdato) {
        this.reservationsudloebsdato = reservationsudloebsdato;
    }

    @Basic
    @Column(name = "retskrivningskontrol", nullable = true, insertable = true, updatable = true, length = 255)
    public String getRetskrivningskontrol() {
        return retskrivningskontrol;
    }

    public void setRetskrivningskontrol(String retskrivningskontrol) {
        this.retskrivningskontrol = retskrivningskontrol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReserveretVejnavnEntity that = (ReserveretVejnavnEntity) o;

        if (id != that.id) return false;
        if (navneomraade != null ? !navneomraade.equals(that.navneomraade) : that.navneomraade != null) return false;
        if (reservationsudloebsdato != null ? !reservationsudloebsdato.equals(that.reservationsudloebsdato) : that.reservationsudloebsdato != null)
            return false;
        if (reserveretNavn != null ? !reserveretNavn.equals(that.reserveretNavn) : that.reserveretNavn != null)
            return false;
        if (reserveretVejnavnUuid != null ? !reserveretVejnavnUuid.equals(that.reserveretVejnavnUuid) : that.reserveretVejnavnUuid != null)
            return false;
        if (retskrivningskontrol != null ? !retskrivningskontrol.equals(that.retskrivningskontrol) : that.retskrivningskontrol != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (reserveretVejnavnUuid != null ? reserveretVejnavnUuid.hashCode() : 0);
        result = 31 * result + (navneomraade != null ? navneomraade.hashCode() : 0);
        result = 31 * result + (reserveretNavn != null ? reserveretNavn.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (reservationsudloebsdato != null ? reservationsudloebsdato.hashCode() : 0);
        result = 31 * result + (retskrivningskontrol != null ? retskrivningskontrol.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserveret_af_kommune_id", referencedColumnName = "kommune_id", nullable = false)
    public KommuneEntity getReserveretAfKommune() {
        return reserveretAfKommune;
    }

    public void setReserveretAfKommune(KommuneEntity reserveretAfKommune) {
        this.reserveretAfKommune = reserveretAfKommune;
    }
}
