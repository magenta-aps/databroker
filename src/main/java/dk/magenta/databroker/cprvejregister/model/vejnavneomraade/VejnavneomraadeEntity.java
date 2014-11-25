package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
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
@Table(name = "vejnavneomraade")
public class VejnavneomraadeEntity
        extends DobbeltHistorikBase<VejnavneomraadeEntity, VejnavneomraadeVersionEntity>
        implements Serializable {

    @Basic
    @Column(name = "vejnavneomraade", nullable = false, insertable = true, updatable = true, columnDefinition="Text")
    private String vejnavneomraade;

    @Basic
    @Column(name = "vejnavnelinje", nullable = true, insertable = true, updatable = true, length = 255)
    private String vejnavnelinje;

    @Basic
    @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
    private String noejagtighedsklasse;

    @Basic
    @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
    private String kilde;

    @Basic
    @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
    private String tekniskStandard;

    @OneToOne(mappedBy = "vejnavneomraade")
    private NavngivenVejEntity navngivenVej;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vejtilslutningspunkt_id", nullable = false)
    private IsoPunktEntity vejtilslutningspunkt;


    @OneToMany(mappedBy = "entity")
    private Collection<VejnavneomraadeVersionEntity> versioner;

    @OneToOne
    private VejnavneomraadeVersionEntity latestVersion;

    @OneToOne
    private VejnavneomraadeVersionEntity preferredVersion;


    protected VejnavneomraadeEntity() {
        this.versioner = new ArrayList<VejnavneomraadeVersionEntity>();
    }

    public String getVejnavneomraade() {
        return this.vejnavneomraade;
    }

    public void setVejnavneomraade(String vejnavneomraade) {
        this.vejnavneomraade = vejnavneomraade;
    }

    public String getVejnavnelinje() {
        return this.vejnavnelinje;
    }

    public void setVejnavnelinje(String vejnavnelinje) {
        this.vejnavnelinje = vejnavnelinje;
    }

    public String getNoejagtighedsklasse() {
        return this.noejagtighedsklasse;
    }

    public void setNoejagtighedsklasse(String noejagtighedsklasse) {
        this.noejagtighedsklasse = noejagtighedsklasse;
    }

    public String getKilde() {
        return this.kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public String getTekniskStandard() {
        return this.tekniskStandard;
    }

    public void setTekniskStandard(String tekniskStandard) {
        this.tekniskStandard = tekniskStandard;
    }

    public NavngivenVejEntity getNavngivenVeje() {
        return this.navngivenVej;
    }

    public void setNavngivenVeje(NavngivenVejEntity navngivneVeje) {
        this.navngivenVej = navngivenVej;
    }

    public IsoPunktEntity getVejtilslutningspunkt() {
        return this.vejtilslutningspunkt;
    }

    public void setVejtilslutningspunkt(IsoPunktEntity vejtilslutningspunkt) {
        this.vejtilslutningspunkt = vejtilslutningspunkt;
    }

    public static VejnavneomraadeEntity create() {
        VejnavneomraadeEntity entity = new VejnavneomraadeEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.vejnavneomraadeRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }

        VejnavneomraadeEntity that = (VejnavneomraadeEntity) o;

        if (this.kilde != null ? !this.kilde.equals(that.kilde) : that.kilde != null) {
            return false;
        }
        if (this.noejagtighedsklasse != null ? !this.noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null) {
            return false;
        }
        if (this.tekniskStandard != null ? !this.tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null) {
            return false;
        }
        if (this.vejnavnelinje != null ? !this.vejnavnelinje.equals(that.vejnavnelinje) : that.vejnavnelinje != null) {
            return false;
        }
        if (this.vejnavneomraade != null ? !this.vejnavneomraade.equals(that.vejnavneomraade) : that.vejnavneomraade != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        result = 31 * result + (this.vejnavneomraade != null ? this.vejnavneomraade.hashCode() : 0);
        result = 31 * result + (this.vejnavnelinje != null ? this.vejnavnelinje.hashCode() : 0);
        result = 31 * result + (this.noejagtighedsklasse != null ? this.noejagtighedsklasse.hashCode() : 0);
        result = 31 * result + (this.kilde != null ? this.kilde.hashCode() : 0);
        result = 31 * result + (this.tekniskStandard != null ? this.tekniskStandard.hashCode() : 0);
        return (int) result;
    }


    @Override
    public Collection<VejnavneomraadeVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public VejnavneomraadeVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(VejnavneomraadeVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public VejnavneomraadeVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(VejnavneomraadeVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected VejnavneomraadeVersionEntity createVersionEntity() {
        return new VejnavneomraadeVersionEntity(this);
    }
}
