package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "vejnavneforslag")
public class VejnavneforslagEntity {
    private int id;
    private String navn;
    private NavngivenVejEntity navngivenVej;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vejnavneforslag_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 255)
    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VejnavneforslagEntity that = (VejnavneforslagEntity) o;

        if (id != that.id) return false;
        if (navn != null ? !navn.equals(that.navn) : that.navn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (navn != null ? navn.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navngiven_vej_id", referencedColumnName = "navngiven_vej_id", nullable = false)
    public NavngivenVejEntity getNavngivenVej() {
        return navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }
}
