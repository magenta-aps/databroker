package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.util.Util;
import org.hibernate.annotations.Index;

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

    // TODO: type?
    private String objekttype;

    public String getObjekttype() {
        return objekttype;
    }

    public void setObjekttype(String objekttype) {
        this.objekttype = objekttype;
    }

    //----------------------------------------------------

    @Column
    private String etage;

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    //----------------------------------------------------

    @Column
    private String doer;

    public String getDoer() {
        return doer;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }

    //----------------------------------------------------------------

    @Column(nullable = false)
    @Index(name = "descriptor")
    private String descriptor; // A field containing kommunekode,vejkode,husnr,etage and doer, for quick lookup

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static String buildDescriptor(int kommuneKode, int vejKode, String husnr, String etage, String doer) {
        return (kommuneKode + ":" + vejKode + ":" + Util.emptyIfNull(husnr) + ":" + Util.emptyIfNull(etage) + ":" + Util.emptyIfNull(doer)).toLowerCase();
    }



    public String getFoo() {
        return Util.emptyIfNull(this.getEtage())+":"+Util.emptyIfNull(this.getDoer());
    }
}
