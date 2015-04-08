package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.util.Util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_enhedsadresse_version")
public class EnhedsAdresseVersionEntity
        extends DobbeltHistorikVersion<EnhedsAdresseEntity, EnhedsAdresseVersionEntity> {

    public EnhedsAdresseVersionEntity() {
    }

    public EnhedsAdresseVersionEntity(EnhedsAdresseEntity entitet) {
        super(entitet);
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false)
    private EnhedsAdresseEntity entity;

    @Override
    public EnhedsAdresseEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(EnhedsAdresseEntity entity) {
        this.entity = entity;
    }

    //----------------------------------------------------

    @Column
    private String kaldenavn;

    public String getKaldenavn() {
        return kaldenavn;
    }

    public void setKaldenavn(String kaldenavn) {
        this.kaldenavn = kaldenavn;
    }

    //----------------------------------------------------

    // TODO: type?
    private String objekttype;

    public String getObjekttype() {
        return objekttype;
    }

    public void setObjekttype(String objekttype) {
        this.objekttype = objekttype;
    }

    //----------------------------------------------------

    public boolean matches(String kaldenavn) {
        return (kaldenavn == null || Util.compare(this.kaldenavn, kaldenavn));
    }
}
