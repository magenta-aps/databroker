package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.VejnavneforslagEntity;
import dk.magenta.databroker.cprvejregister.model.VejnavneomraadeEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "navngiven_vej")
public class NavngivenVejEntity
        extends DobbeltHistorikEntity<NavngivenVejEntity, NavngivenVejRegistreringEntity, NavngivenVejRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "vejnavn", nullable = true, insertable = true, updatable = true, length = 255)
    private String vejnavn;

    @Basic
    @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "vejaddresseringsnavn", nullable = true, insertable = true, updatable = true, length = 20)
    private String vejaddresseringsnavn;

    @Basic
    @Column(name = "beskrivelse", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    private String beskrivelse;

    @Basic
    @Column(name = "retskrivningskontrol", nullable = true, insertable = true, updatable = true, length = 255)
    private String retskrivningskontrol;

    @OneToMany(mappedBy = "navngivenVej")
    private Collection<HusnummerEntity> husnumre;

    @OneToMany(mappedBy = "navngivenVej")
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ansvarlig_kommune_id", referencedColumnName = "kommune_id", nullable = false)
    private KommuneEntity ansvarligKommune;

    @OneToOne
    @JoinColumn(name = "vejnavneomraade_id", referencedColumnName = "vejnavneomraade_id", nullable = true)
    private VejnavneomraadeEntity vejnavneomraade;

    @OneToMany(mappedBy = "navngivenVej")
    private Collection<VejnavneforslagEntity> vejnavneforslag;


    public String getVejnavn() {
        return this.vejnavn;
    }

    public void setVejnavn(String vejnavn) {
        this.vejnavn = vejnavn;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVejaddresseringsnavn() {
        return this.vejaddresseringsnavn;
    }

    public void setVejaddresseringsnavn(String vejaddresseringsnavn) {
        this.vejaddresseringsnavn = vejaddresseringsnavn;
    }

    public String getBeskrivelse() {
        return this.beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getRetskrivningskontrol() {
        return this.retskrivningskontrol;
    }

    public void setRetskrivningskontrol(String retskrivningskontrol) {
        this.retskrivningskontrol = retskrivningskontrol;
    }

    public Collection<HusnummerEntity> getHusnumre() {
        return husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
    }

    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return this.kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    public KommuneEntity getAnsvarligKommune() {
        return this.ansvarligKommune;
    }

    public void setAnsvarligKommune(KommuneEntity ansvarligKommune) {
        this.ansvarligKommune = ansvarligKommune;
    }

    public VejnavneomraadeEntity getVejnavneomraade() {
        return this.vejnavneomraade;
    }

    public void setVejnavneomraade(VejnavneomraadeEntity vejnavneomraade) {
        this.vejnavneomraade = vejnavneomraade;
    }

    public Collection<VejnavneforslagEntity> getVejnavneforslag() {
        return this.vejnavneforslag;
    }

    public void setVejnavneforslag(Collection<VejnavneforslagEntity> vejnavneforslag) {
        this.vejnavneforslag = vejnavneforslag;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!super.equals(other)) {
            return false;
        }

        NavngivenVejEntity that = (NavngivenVejEntity) other;

        if (this.beskrivelse != null ? !this.beskrivelse.equals(that.beskrivelse) : that.beskrivelse != null) {
            return false;
        }
        if (this.retskrivningskontrol != null ? !this.retskrivningskontrol.equals(that.retskrivningskontrol) : that.retskrivningskontrol != null) {
            return false;
        }
        if (this.status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (this.vejaddresseringsnavn != null ? !this.vejaddresseringsnavn.equals(that.vejaddresseringsnavn) : that.vejaddresseringsnavn != null) {
            return false;
        }
        if (this.vejnavn != null ? !this.vejnavn.equals(that.vejnavn) : that.vejnavn != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.vejnavn != null ? this.vejnavn.hashCode() : 0);
        result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
        result = 31 * result + (this.vejaddresseringsnavn != null ? this.vejaddresseringsnavn.hashCode() : 0);
        result = 31 * result + (this.beskrivelse != null ? this.beskrivelse.hashCode() : 0);
        result = 31 * result + (this.retskrivningskontrol != null ? this.retskrivningskontrol.hashCode() : 0);
        return (int) result;
    }

}
