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

    public EjerLavVersionEntity() {
    }

    public EjerLavVersionEntity(EjerLavEntity entitet) {
        super(entitet);
    }

    //------------------------------------------------------------------------------------------------------------------

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

    //------------------------------------------------------------------------------------------------------------------

    @Column
    private int kode;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    //----------------------------------------------------

    @Column
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
