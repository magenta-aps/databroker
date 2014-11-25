package dk.magenta.databroker.cprvejregister.model.vejnavneforslag;

import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneforslag", indexes = { @Index(name="navn", columnList="navn") } )
public class VejnavneforslagEntity implements Serializable {

    public VejnavneforslagEntity() {

    }

    public static VejnavneforslagEntity create() {
        return new VejnavneforslagEntity();
    }


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 255)
    private String navn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navngiven_vej_id", nullable = false)
    private NavngivenVejEntity navngivenVej;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNavn() {
        return this.navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) {
            return false;
        }
        VejnavneforslagEntity that = (VejnavneforslagEntity) o;
        if (this.navn != null ? !this.navn.equals(that.navn) : that.navn != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (navn != null ? navn.hashCode() : 0);
        return (int) result;
    }

}
