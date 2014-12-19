package dk.magenta.databroker.dawa.model.temaer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_region")
public class RegionEntity extends TemaBase {
    @Column(nullable = false)
    private int kode;

    @OneToMany(mappedBy = "region")
    private Collection<KommuneEntity> kommuner;

    @Override
    public TemaType getTemaType() {
        return TemaType.REGION;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public Collection<KommuneEntity> getKommuner() {
        return kommuner;
    }

    public void setKommuner(Collection<KommuneEntity> kommuner) {
        this.kommuner = kommuner;
    }
}
