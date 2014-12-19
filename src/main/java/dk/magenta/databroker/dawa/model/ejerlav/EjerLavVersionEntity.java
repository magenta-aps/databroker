package dk.magenta.databroker.dawa.model.ejerlav;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_ejerlav_version")
public class EjerLavVersionEntity extends DobbeltHistorikVersion<EjerLavEntity, EjerLavVersionEntity> {
    @ManyToOne(optional = false)
    private EjerLavEntity entity;

    @Override
    public EjerLavEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(EjerLavEntity entity) {
        this.entity = entity;
    }

    public EjerLavVersionEntity() {

    }

    public EjerLavVersionEntity(EjerLavEntity entitet) {
        super(entitet);
    }


    @Column
    private int kode;
    @Column
    private String navn;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
