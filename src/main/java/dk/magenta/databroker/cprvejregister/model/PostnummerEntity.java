package dk.magenta.databroker.cprvejregister.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "postnummer", indexes = { @Index(name="nummer", columnList="nummer") })
public class PostnummerEntity {
    private int id;
    private int nummer;
    private String navn;
    private Collection<AdgangspunktEntity> adgangspunkter;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "postnummer_id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 36)
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

        PostnummerEntity that = (PostnummerEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @OneToMany(mappedBy = "liggerIPostnummer")
    public Collection<AdgangspunktEntity> getAdgangspunkter() {
        return adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }
}
