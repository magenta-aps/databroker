package dk.magenta.databroker.cprvejregister.model.isopunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "isopunkt")
public class IsoPunktEntity extends DobbeltHistorikBase<IsoPunktEntity, IsoPunktRegistreringEntity, IsoPunktRegistreringsVirkningEntity>
        implements Serializable {

    @Basic
    @Column(name = "x", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double x;

    @Basic
    @Column(name = "y", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double y;

    @Basic
    @Column(name = "z", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double z;

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return this.z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public static IsoPunktEntity create() {
        IsoPunktEntity entity = new IsoPunktEntity();
        entity.generateNewUUID();
        return entity;
    }

    protected IsoPunktRegistreringEntity createRegistreringEntity(RegistreringEntity oioRegistrering, List<VirkningEntity> virkninger) {
        return new IsoPunktRegistreringEntity(this, oioRegistrering, virkninger);
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.isoPunktRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }

        IsoPunktEntity that = (IsoPunktEntity) o;

        if (this.x != null ? !this.x.equals(that.x) : that.x != null) {
            return false;
        }
        if (this.y != null ? !this.y.equals(that.y) : that.y != null) {
            return false;
        }
        if (this.z != null ? !this.z.equals(that.z) : that.z != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.x != null ? this.x.hashCode() : 0);
        result = 31 * result + (this.y != null ? this.y.hashCode() : 0);
        result = 31 * result + (this.z != null ? this.z.hashCode() : 0);
        return (int) result;
    }
}
