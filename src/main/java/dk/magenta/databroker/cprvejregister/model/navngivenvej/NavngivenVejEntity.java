package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;
import dk.magenta.databroker.cprvejregister.model.vejnavneomraade.VejnavneomraadeEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "navngiven_vej" )
public class NavngivenVejEntity
        extends DobbeltHistorikBase<NavngivenVejEntity, NavngivenVejVersionEntity>
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




    @OneToMany(mappedBy = "entitet")
    private Collection<NavngivenVejVersionEntity> versioner;

    @OneToOne
    private NavngivenVejVersionEntity latestVersion;

    @OneToOne
    private NavngivenVejVersionEntity preferredVersion;


    protected NavngivenVejEntity(){
        this.versioner = new ArrayList<NavngivenVejVersionEntity>();
    }



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
    public Collection<NavngivenVejVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public NavngivenVejVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(NavngivenVejVersionEntity newLatest) {
        this.latestVersion = newLatest;
    }

    @Override
    public NavngivenVejVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(NavngivenVejVersionEntity newPreferred) {
        this.preferredVersion = newPreferred;
    }

    @Override
    protected NavngivenVejVersionEntity createVersionEntity() {
        return new NavngivenVejVersionEntity(this);
    }
}
