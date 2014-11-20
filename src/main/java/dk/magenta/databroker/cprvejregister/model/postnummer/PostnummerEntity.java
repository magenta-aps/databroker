package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.AdgangspunktEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "postnummer", indexes = { @Index(name="nummer", columnList="nummer") })
public class PostnummerEntity
        extends DobbeltHistorikEntity<PostnummerEntity, PostnummerRegistreringEntity, PostnummerRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
    private int nummer;

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 36)
    private String navn;

    @OneToMany(mappedBy = "liggerIPostnummer")
    private Collection<AdgangspunktEntity> adgangspunkter;


    public int getNummer() {
        return this.nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Collection<AdgangspunktEntity> getAdgangspunkter() {
        return this.adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) (long) this.getId();
    }

}
