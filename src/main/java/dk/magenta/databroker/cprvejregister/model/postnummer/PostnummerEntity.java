package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
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


    protected PostnummerEntity() {
        this.versioner = new ArrayList<PostnummerVersionEntity>();
    }

    public static PostnummerEntity create() {
        PostnummerEntity entity = new PostnummerEntity();
        entity.generateNewUUID();
        return entity;
    }

    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
    private Collection<PostnummerVersionEntity> versioner;

    @OneToOne(fetch = FetchType.LAZY)
    private PostnummerVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private PostnummerVersionEntity preferredVersion;

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


    /*
    * Create the relevant version entity
    * */

     @Override
    protected PostnummerVersionEntity createVersionEntity() {
        return new PostnummerVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
    private int nummer;

    @OneToMany(mappedBy = "liggerIPostnummer", fetch = FetchType.LAZY)
    private Collection<AdgangspunktVersionEntity> adgangspunkter;

    public int getNummer() {
        return this.nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public Collection<AdgangspunktVersionEntity> getAdgangspunkter() {
        return this.adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktVersionEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.postnummerRepository;
    }

}
