package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommune")
public class KommuneEntity implements Serializable {
    private int kommuneId;
    private int kommunekode;
    private String navn;
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;
    private Collection<NavngivenVejEntity> ansvarligForNavngivneVeje;
    private Collection<ReserveretVejnavnEntity> reserveredeVejnavne;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "kommune_id", nullable = false, insertable = false, updatable = false)
    public int getKommuneId() {
        return kommuneId;
    }

    public void setKommuneId(int kommuneId) {
        this.kommuneId = kommuneId;
    }

    @Basic
    @Column(name = "kommunekode", nullable = false, insertable = true, updatable = true)
    public int getKommunekode() {
        return kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.kommunekode = kommunekode;
    }

    @Basic
    @Column(name = "navn", nullable = false, insertable = true, updatable = true, length = 255)
    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KommuneEntity that = (KommuneEntity) o;

        if (kommuneId != that.kommuneId) return false;
        if (kommunekode != that.kommunekode) return false;
        if (navn != null ? !navn.equals(that.navn) : that.navn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kommuneId;
        result = 31 * result + kommunekode;
        result = 31 * result + (navn != null ? navn.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "kommune")
    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    @OneToMany(mappedBy = "ansvarligKommune")
    public Collection<NavngivenVejEntity> getAnsvarligForNavngivneVeje() {
        return ansvarligForNavngivneVeje;
    }

    public void setAnsvarligForNavngivneVeje(Collection<NavngivenVejEntity> ansvarligForNavngivneVeje) {
        this.ansvarligForNavngivneVeje = ansvarligForNavngivneVeje;
    }

    @OneToMany(mappedBy = "reserveretAfKommune")
    public Collection<ReserveretVejnavnEntity> getReserveredeVejnavne() {
        return reserveredeVejnavne;
    }

    public void setReserveredeVejnavne(Collection<ReserveretVejnavnEntity> reserveredeVejnavne) {
        this.reserveredeVejnavne = reserveredeVejnavne;
    }
}
