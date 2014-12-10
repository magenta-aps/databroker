package dk.magenta.databroker.cprvejregister.model.isopunkt;

import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "isopunkt")
public class IsoPunktEntity implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "x", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double x;

    @Basic
    @Column(name = "y", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double y;

    @Basic
    @Column(name = "z", nullable = true, insertable = true, updatable = true, precision = 0)
    private Double z;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return new IsoPunktEntity();
    }

}
