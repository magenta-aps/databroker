package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.ReserveretVejnavnEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommune", indexes = { @Index(name = "kommunekode", columnList = "kommunekode") })
public class KommuneEntity
        extends DobbeltHistorikEntity<KommuneEntity, KommuneRegistreringEntity, KommuneRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "kommunekode", nullable = false, insertable = true, updatable = true)
    private int kommunekode;

    @Basic
    @Column(name = "navn", nullable = false, insertable = true, updatable = true, length = 255)
    private String navn;

    @OneToMany(mappedBy = "kommune")
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

    @OneToMany(mappedBy = "ansvarligKommune")
    private Collection<NavngivenVejEntity> ansvarligForNavngivneVeje;

    @OneToMany(mappedBy = "reserveretAfKommune")
    private Collection<ReserveretVejnavnEntity> reserveredeVejnavne;

    public int getKommunekode() {
        return this.kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.kommunekode = kommunekode;
    }

    public String getNavn() {
        return this.navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return this.kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    public Collection<NavngivenVejEntity> getAnsvarligForNavngivneVeje() {
        return this.ansvarligForNavngivneVeje;
    }

    public void setAnsvarligForNavngivneVeje(Collection<NavngivenVejEntity> ansvarligForNavngivneVeje) {
        this.ansvarligForNavngivneVeje = ansvarligForNavngivneVeje;
    }

    public Collection<ReserveretVejnavnEntity> getReserveredeVejnavne() {
        return this.reserveredeVejnavne;
    }

    public void setReserveredeVejnavne(Collection<ReserveretVejnavnEntity> reserveredeVejnavne) {
        this.reserveredeVejnavne = reserveredeVejnavne;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }

        KommuneEntity that = (KommuneEntity) other;

        if (this.kommunekode != that.kommunekode) {
            return false;
        }
        if (this.navn != null ? !this.navn.equals(that.navn) : that.navn != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + this.kommunekode;
        result = 31 * result + (this.navn != null ? this.navn.hashCode() : 0);
        return (int) result;
    }

}