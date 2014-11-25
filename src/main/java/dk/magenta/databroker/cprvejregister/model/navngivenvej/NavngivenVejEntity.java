package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;
import dk.magenta.databroker.cprvejregister.model.vejnavneomraade.VejnavneomraadeEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "navngiven_vej" )
public class NavngivenVejEntity
        extends DobbeltHistorikBase<NavngivenVejEntity, NavngivenVejVersionEntity, NavngivenVejRegistreringsVirkningEntity>
        implements Serializable {

    @OneToMany(mappedBy = "navngivenVej")
    private Collection<HusnummerEntity> husnumre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ansvarlig_kommune_id", nullable = false)
    private KommuneEntity ansvarligKommune;

    @OneToOne
    @JoinColumn(name = "vejnavneomraade_id", nullable = true)
    private VejnavneomraadeEntity vejnavneomraade;

    @OneToMany(mappedBy = "navngivenVej")
    private Collection<VejnavneforslagEntity> vejnavneforslag;


    public Collection<HusnummerEntity> getHusnumre() {
        return husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
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


    public static NavngivenVejEntity create() {
        NavngivenVejEntity entity = new NavngivenVejEntity();
        entity.generateNewUUID();
        return entity;
    }

    @Override
    public int hashCode() {
        return (this.getUuid() != null ? this.getUuid().hashCode() : 0);
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.navngivenVejRepository;
    }

    @Override
    protected NavngivenVejVersionEntity createRegistreringEntity() {
        return new NavngivenVejVersionEntity(this);
    }

    public NavngivenVejVersionEntity addRegistrering(
            String vejnavn, String status, String vejaddresseringsnavn, String beskrivelse, String retskrivningskontrol,
            RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger
    ) {
        NavngivenVejVersionEntity newReg = super.addRegistrering(fromOIORegistrering, virkninger);
        newReg.setVejnavn(vejnavn);
        newReg.setStatus(status);
        newReg.setVejaddresseringsnavn(vejaddresseringsnavn);
        newReg.setBeskrivelse(beskrivelse);
        newReg.setRetskrivningskontrol(retskrivningskontrol);
        return newReg;
    }
}
