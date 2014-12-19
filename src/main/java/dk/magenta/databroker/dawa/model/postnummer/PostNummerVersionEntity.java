package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_postnummer_version")
public class PostNummerVersionEntity extends DobbeltHistorikVersion<PostNummerEntity, PostNummerVersionEntity> {

    @ManyToOne(optional = false)
    private PostNummerEntity entity;

    @Column(nullable = false)
    private int nr;

    @Column(nullable = false)
    private String navn;

    @Column(nullable = false)
    private boolean stormodtager = false;

    @Override
    public PostNummerEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(PostNummerEntity entity) {
        this.entity = entity;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public boolean isStormodtager() {
        return stormodtager;
    }

    public void setStormodtager(boolean stormodtager) {
        this.stormodtager = stormodtager;
    }

    public PostNummerVersionEntity() {
    }

    public PostNummerVersionEntity(PostNummerEntity entitet) {
        super(entitet);
    }

    @ManyToMany
    private Collection<KommuneEntity> kommuner;

    public Collection<KommuneEntity> getKommuner() {
        return kommuner;
    }

    public void setKommuner(Collection<KommuneEntity> kommuner) {
        this.kommuner = kommuner;
    }
}
