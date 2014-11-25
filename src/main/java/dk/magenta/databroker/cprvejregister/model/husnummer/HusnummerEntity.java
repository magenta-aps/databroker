package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "husnummer")
public class HusnummerEntity extends UniqueBase implements Serializable {

    public HusnummerEntity() {
    }

    public static HusnummerEntity create() {
        HusnummerEntity entity = new HusnummerEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Fields on the entity
    * */

    @OneToOne(mappedBy = "husnummer", fetch = FetchType.LAZY)
    private AdgangspunktEntity adgangspunkt;

    @OneToMany(mappedBy = "husnummer", fetch = FetchType.LAZY)
    private Collection<AdresseEntity> adresser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private NavngivenVejEntity navngivenVej;

    @Basic
    @Column(nullable = true)
    private String husnummerbetegnelse;

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }

    public String getHusnummerbetegnelse() {
        return husnummerbetegnelse;
    }

    public void setHusnummerbetegnelse(String husnummerbetegnelse) {
        this.husnummerbetegnelse = husnummerbetegnelse;
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
