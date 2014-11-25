package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
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
@Table(name = "husnummer", indexes = { @Index(name="navngivenVej", columnList="navngiven_vej_id"), @Index(name="adgangspunkt", columnList="tilknyttet_adgangspunkt_id") })
public class HusnummerEntity
        extends DobbeltHistorikBase<HusnummerEntity, HusnummerVersionEntity>
        implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navngiven_vej_id", nullable = false)
    private NavngivenVejEntity navngivenVej;






    @OneToMany(mappedBy = "entity")
    private Collection<HusnummerVersionEntity> versioner;

    @OneToOne
    private HusnummerVersionEntity latestVersion;

    @OneToOne
    private HusnummerVersionEntity preferredVersion;


    protected HusnummerEntity() {
        this.versioner = new ArrayList<HusnummerVersionEntity>();
    }



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
    public Collection<HusnummerVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public HusnummerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(HusnummerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public HusnummerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(HusnummerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected HusnummerVersionEntity createVersionEntity() {
        return new HusnummerVersionEntity(this);
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
