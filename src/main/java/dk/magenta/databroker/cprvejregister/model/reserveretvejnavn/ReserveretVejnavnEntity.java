package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "reserveret_vejnavn")
public class ReserveretVejnavnEntity
        extends DobbeltHistorikBase<ReserveretVejnavnEntity, ReserveretVejnavnVersionEntity, ReserveretVejnavnRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "navneomraade", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    private String navneomraade;

    @Basic
    @Column(name = "reserveret_navn", nullable = true, insertable = true, updatable = true, length = 255)
    private String reserveretNavn;

    @Basic
    @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "reservationsudloebsdato", nullable = true, insertable = true, updatable = true)
    private Date reservationsudloebsdato;

    @Basic
    @Column(name = "retskrivningskontrol", nullable = true, insertable = true, updatable = true, length = 255)
    private String retskrivningskontrol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserveret_af_kommune_id", nullable = false)
    private KommuneEntity reserveretAfKommune;


    public String getNavneomraade() {
        return navneomraade;
    }

    public void setNavneomraade(String navneomraade) {
        this.navneomraade = navneomraade;
    }

    public String getReserveretNavn() {
        return reserveretNavn;
    }

    public void setReserveretNavn(String reserveretNavn) {
        this.reserveretNavn = reserveretNavn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReservationsudloebsdato() {
        return this.reservationsudloebsdato;
    }

    public void setReservationsudloebsdato(Date reservationsudloebsdato) {
        this.reservationsudloebsdato = reservationsudloebsdato;
    }

    public String getRetskrivningskontrol() {
        return this.retskrivningskontrol;
    }

    public void setRetskrivningskontrol(String retskrivningskontrol) {
        this.retskrivningskontrol = retskrivningskontrol;
    }

    public KommuneEntity getReserveretAfKommune() {
        return this.reserveretAfKommune;
    }

    public void setReserveretAfKommune(KommuneEntity reserveretAfKommune) {
        this.reserveretAfKommune = reserveretAfKommune;
    }


    public static ReserveretVejnavnEntity create() {
        ReserveretVejnavnEntity entity = new ReserveretVejnavnEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.reserveretVejnavnRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }

        ReserveretVejnavnEntity that = (ReserveretVejnavnEntity) o;

        if (this.navneomraade != null ? !this.navneomraade.equals(that.navneomraade) : that.navneomraade != null) {
            return false;
        }
        if (this.reservationsudloebsdato != null ? !this.reservationsudloebsdato.equals(that.reservationsudloebsdato) : that.reservationsudloebsdato != null) {
            return false;
        }
        if (this.reserveretNavn != null ? !this.reserveretNavn.equals(that.reserveretNavn) : that.reserveretNavn != null) {
            return false;
        }
        if (this.retskrivningskontrol != null ? !this.retskrivningskontrol.equals(that.retskrivningskontrol) : that.retskrivningskontrol != null) {
            return false;
        }
        if (this.status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.navneomraade != null ? this.navneomraade.hashCode() : 0);
        result = 31 * result + (this.reserveretNavn != null ? this.reserveretNavn.hashCode() : 0);
        result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
        result = 31 * result + (this.reservationsudloebsdato != null ? this.reservationsudloebsdato.hashCode() : 0);
        result = 31 * result + (this.retskrivningskontrol != null ? this.retskrivningskontrol.hashCode() : 0);
        return (int) result;
    }

    @Override
    protected ReserveretVejnavnVersionEntity createRegistreringEntity() {
        return new ReserveretVejnavnVersionEntity(this);
    }
}
