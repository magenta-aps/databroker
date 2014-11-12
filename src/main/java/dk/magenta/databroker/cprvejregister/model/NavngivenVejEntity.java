package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "navngiven_vej")
public class NavngivenVejEntity {
    private int id;
    private String navngivenVejUuid;
    private String vejnavn;
    private String status;
    private String vejaddresseringsnavn;
    private String beskrivelse;
    private String retskrivningskontrol;
    private Collection<HusnummerEntity> husnumre;
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;
    private KommuneEntity ansvarligKommune;
    private VejnavneomraadeEntity vejnavneomraade;
    private Collection<VejnavneforslagEntity> vejnavneforslag;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "navngiven_vej_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "navngiven_vej_uuid", nullable = false, insertable = true, updatable = true, length = 36)
    public String getNavngivenVejUuid() {
        return navngivenVejUuid;
    }

    public void setNavngivenVejUuid(String navngivenVejUuid) {
        this.navngivenVejUuid = navngivenVejUuid;
    }

    @Basic
    @Column(name = "vejnavn", nullable = true, insertable = true, updatable = true, length = 255)
    public String getVejnavn() {
        return vejnavn;
    }

    public void setVejnavn(String vejnavn) {
        this.vejnavn = vejnavn;
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
    @Column(name = "vejaddresseringsnavn", nullable = true, insertable = true, updatable = true, length = 20)
    public String getVejaddresseringsnavn() {
        return vejaddresseringsnavn;
    }

    public void setVejaddresseringsnavn(String vejaddresseringsnavn) {
        this.vejaddresseringsnavn = vejaddresseringsnavn;
    }

    @Basic
    @Column(name = "beskrivelse", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
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

        NavngivenVejEntity that = (NavngivenVejEntity) o;

        if (id != that.id) return false;
        if (beskrivelse != null ? !beskrivelse.equals(that.beskrivelse) : that.beskrivelse != null) return false;
        if (navngivenVejUuid != null ? !navngivenVejUuid.equals(that.navngivenVejUuid) : that.navngivenVejUuid != null)
            return false;
        if (retskrivningskontrol != null ? !retskrivningskontrol.equals(that.retskrivningskontrol) : that.retskrivningskontrol != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (vejaddresseringsnavn != null ? !vejaddresseringsnavn.equals(that.vejaddresseringsnavn) : that.vejaddresseringsnavn != null)
            return false;
        if (vejnavn != null ? !vejnavn.equals(that.vejnavn) : that.vejnavn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (navngivenVejUuid != null ? navngivenVejUuid.hashCode() : 0);
        result = 31 * result + (vejnavn != null ? vejnavn.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (vejaddresseringsnavn != null ? vejaddresseringsnavn.hashCode() : 0);
        result = 31 * result + (beskrivelse != null ? beskrivelse.hashCode() : 0);
        result = 31 * result + (retskrivningskontrol != null ? retskrivningskontrol.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "navngivenVej")
    public Collection<HusnummerEntity> getHusnumre() {
        return husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
    }

    @OneToMany(mappedBy = "navngivenVej")
    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    @ManyToOne
    @JoinColumn(name = "ansvarlig_kommune_id", referencedColumnName = "kommune_id", nullable = false)
    public KommuneEntity getAnsvarligKommune() {
        return ansvarligKommune;
    }

    public void setAnsvarligKommune(KommuneEntity ansvarligKommune) {
        this.ansvarligKommune = ansvarligKommune;
    }

    @OneToOne
    @JoinColumn(name = "vejnavneomraade_id", referencedColumnName = "vejnavneomraade_id", nullable = false)
    public VejnavneomraadeEntity getVejnavneomraade() {
        return vejnavneomraade;
    }

    public void setVejnavneomraade(VejnavneomraadeEntity vejnavneomraade) {
        this.vejnavneomraade = vejnavneomraade;
    }

    @OneToMany(mappedBy = "navngivenVej")
    public Collection<VejnavneforslagEntity> getVejnavneforslag() {
        return vejnavneforslag;
    }

    public void setVejnavneforslag(Collection<VejnavneforslagEntity> vejnavneforslag) {
        this.vejnavneforslag = vejnavneforslag;
    }
}
