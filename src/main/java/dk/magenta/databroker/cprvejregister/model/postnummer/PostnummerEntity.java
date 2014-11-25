package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "postnummer", indexes = { @Index(name="nummer", columnList="nummer") })
public class PostnummerEntity
        extends DobbeltHistorikBase<PostnummerEntity, PostnummerVersionEntity>
        implements Serializable {

    @Basic
    @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
    private int nummer;

    @Basic
    @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 36)
    private String navn;

    @OneToMany(mappedBy = "liggerIPostnummer")
    private Collection<AdgangspunktEntity> adgangspunkter;




    @OneToMany(mappedBy = "entity")
    private Collection<PostnummerVersionEntity> versioner;

    @OneToOne
    private PostnummerVersionEntity latestVersion;

    @OneToOne
    private PostnummerVersionEntity preferredVersion;


    protected PostnummerEntity() {
        this.versioner = new ArrayList<PostnummerVersionEntity>();
    }



    public int getNummer() {
        return this.nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Collection<AdgangspunktEntity> getAdgangspunkter() {
        return this.adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }


    public static PostnummerEntity create() {
        PostnummerEntity entity = new PostnummerEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.postnummerRepository;
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
        return (int) (long) this.getId();
    }


    @Override
    public Collection<PostnummerVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public PostnummerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(PostnummerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public PostnummerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(PostnummerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected PostnummerVersionEntity createVersionEntity() {
        return new PostnummerVersionEntity(this);
    }
}
