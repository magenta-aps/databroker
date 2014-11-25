package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRegistreringEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "husnummer", indexes = { @Index(name="navngivenVej", columnList="navngiven_vej_id"), @Index(name="adgangspunkt", columnList="tilknyttet_adgangspunkt_id") })
public class HusnummerEntity
        extends DobbeltHistorikBase<HusnummerEntity, HusnummerVersionEntity, HusnummerRegistreringsVirkningEntity>
        implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navngiven_vej_id", nullable = false)
    private NavngivenVejEntity navngivenVej;

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }


    public static HusnummerEntity create() {
        HusnummerEntity entity = new HusnummerEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.husnummerRepository;
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
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        return (int) result;
    }

    @Override
    protected HusnummerVersionEntity createRegistreringEntity() {
        return new HusnummerVersionEntity(this);
    }


    public HusnummerRegistreringEntity addRegistrering(String husnummerbetegnelse, RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger) {
        HusnummerRegistreringEntity reg = super.addRegistrering(fromOIORegistrering, virkninger);
        reg.setHusnummerbetegnelse(husnummerbetegnelse);
        return reg;
    }
}


/*
@Basic
@Column(name = "husnummerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
private String husnummerbetegnelse;

@OneToMany(mappedBy = "husnummer")
private Collection<AdresseEntity> adresser;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "tilknyttet_adgangspunkt_id", nullable = true)
private AdgangspunktEntity tilknyttetAdgangspunkt;
*/
