package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.dawa.model.postnummer.PostNummerVersionEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_kommune")
public class KommuneEntity extends TemaBase {
    @Column(nullable = false)
    private int kode;

    // Not sure all data sources can supply this
    @ManyToOne(optional = true)
    private RegionEntity region;

    @OneToMany(mappedBy = "kommune")
    private Collection<VejstykkeEntity> vejstykker;

    @ManyToMany(mappedBy = "kommuner")
    private Collection<PostNummerVersionEntity> postnumre;

    @Override
    public TemaType getTemaType() {
        return TemaType.KOMMUNE;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = region;
    }

    public Collection<VejstykkeEntity> getVejstykkeVersioner() {
        return vejstykker;
    }

    public void setVejstykker(Collection<VejstykkeEntity> vejstykker) {
        this.vejstykker = vejstykker;
    }

    public Collection<PostNummerVersionEntity> getPostnumre() {
        return postnumre;
    }

    public void setPostnumre(Collection<PostNummerVersionEntity> postnumre) {
        this.postnumre = postnumre;
    }
}
