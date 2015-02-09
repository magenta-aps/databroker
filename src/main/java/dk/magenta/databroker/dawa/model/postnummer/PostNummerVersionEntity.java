package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_postnummer_version")
public class PostNummerVersionEntity extends DobbeltHistorikVersion<PostNummerEntity, PostNummerVersionEntity> {


    public PostNummerVersionEntity() {
        this.kommuner = new HashSet<KommuneEntity>();
    }

    public PostNummerVersionEntity(PostNummerEntity entitet) {
        super(entitet);
        this.kommuner = new HashSet<KommuneEntity>();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false)
    private PostNummerEntity entity;

    @Override
    public PostNummerEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(PostNummerEntity entity) {
        this.entity = entity;
    }

    //----------------------------------------------------

    @Column(nullable = false)
    @Index(name = "nrIndex")
    private int nr;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    //----------------------------------------------------

    @Column(nullable = false)
    @Index(name = "navnIndex")
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    //----------------------------------------------------

    @Column(nullable = false)
    private boolean stormodtager = false;

    public boolean isStormodtager() {
        return stormodtager;
    }

    public void setStormodtager(boolean stormodtager) {
        this.stormodtager = stormodtager;
    }

    //----------------------------------------------------

    @ManyToMany
    private Collection<KommuneEntity> kommuner;

    public Collection<KommuneEntity> getKommuner() {
        return kommuner;
    }

    public void addKommune(KommuneEntity kommune) {
        if (!this.kommuner.contains(kommune)) {
            this.kommuner.add(kommune);
        }
    }
    public void removeKommune(KommuneEntity kommune) {
        if (this.kommuner.contains(kommune)) {
            this.kommuner.remove(kommune);
        }
    }
}
