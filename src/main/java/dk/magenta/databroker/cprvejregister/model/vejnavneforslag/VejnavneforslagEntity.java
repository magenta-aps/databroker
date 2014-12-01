package dk.magenta.databroker.cprvejregister.model.vejnavneforslag;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneforslag")
public class VejnavneforslagEntity extends UniqueBase implements Serializable {

    public VejnavneforslagEntity() {

    }

    public static VejnavneforslagEntity create() {
        return new VejnavneforslagEntity();
    }


    @Basic
    @Column(nullable = false, insertable = true, updatable = true, length = 255)
    @Index(name="navn")
    private String navn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private NavngivenVejEntity navngivenVej;

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

}
