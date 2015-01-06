package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;

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

    public EnhedsAdresseVersionEntity() {
    }

    public EnhedsAdresseVersionEntity(EnhedsAdresseEntity entitet) {
        super(entitet);
    }

    @ManyToOne(optional = false)
    private AdgangsAdresseEntity adgangsadresse;

    // TODO: type?
    private String objekttype;

    @Column
    private String etage;

    @Column
    private String doer;

    public AdgangsAdresseEntity getAdgangsadresse() {
        return adgangsadresse;
    }

    public void setAdgangsadresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsadresse = adgangsAdresse;
    }

    public String getObjekttype() {
        return objekttype;
    }

    public void setObjekttype(String objekttype) {
        this.objekttype = objekttype;
    }

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    public String getDoer() {
        return doer;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }
}
