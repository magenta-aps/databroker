package dk.magenta.databroker.cprvejregister.model.vejnavneforslag;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRegistreringEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneforslag", indexes = { @Index(name="navn", columnList="navn") } )
public class VejnavneforslagEntity implements Serializable {
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


    public static VejnavneforslagEntity create() {
        return new VejnavneforslagEntity();
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.vejnavneforslagRepository;
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
