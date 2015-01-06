package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.stormodtagere.StormodtagerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.service.rest.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_adgangsadresse")
public class AdgangsAdresseEntity extends DobbeltHistorikBase<AdgangsAdresseEntity, AdgangsAdresseVersionEntity> {

    public AdgangsAdresseEntity() {
        this.versioner = new ArrayList<AdgangsAdresseVersionEntity>();
        this.enhedsAdresseVersioner = new ArrayList<EnhedsAdresseVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Versioning */

    @OneToMany(mappedBy="entity")
    private Collection<AdgangsAdresseVersionEntity> versioner;

    @Override
    public Collection<AdgangsAdresseVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<AdgangsAdresseVersionEntity> versioner) {
        this.versioner = versioner;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AdgangsAdresseVersionEntity latestVersion;

    @Override
    public AdgangsAdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdgangsAdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private AdgangsAdresseVersionEntity preferredVersion;

    @Override
    public AdgangsAdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdgangsAdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected AdgangsAdresseVersionEntity createVersionEntity() {
        return new AdgangsAdresseVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @OneToOne(mappedBy = "adgangsadresse")
    private StormodtagerEntity stormodtager;

    public StormodtagerEntity getStormodtager() {
        return stormodtager;
    }

    public void setStormodtager(StormodtagerEntity stormodtager) {
        this.stormodtager = stormodtager;
    }

    //----------------------------------------------------

    @OneToMany(mappedBy = "adgangsadresse")
    private Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner;

    public Collection<EnhedsAdresseVersionEntity> getEnhedsAdresseVersioner() {
        return enhedsAdresseVersioner;
    }

    public void setEnhedsAdresseVersioner(Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner) {
        this.enhedsAdresseVersioner = enhedsAdresseVersioner;
    }

    //----------------------------------------------------

    @ManyToOne(optional = false)
    private VejstykkeEntity vejstykke;

    public VejstykkeEntity getVejstykke() {
        return vejstykke;
    }

    public void setVejstykke(VejstykkeEntity vejstykke) {
        this.vejstykke = vejstykke;
    }

    //----------------------------------------------------

    @Column(nullable = false)
    private String husnr;

    public String getHusnr() {
        return husnr;
    }

    public void setHusnr(String husnr) {
        this.husnr = husnr;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "adgangsAdresse";
    }
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("husnr", this.getHusnr());
        obj.put("href", SearchService.getAdgangsAdresseBaseUrl()+"/"+this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        VejstykkeEntity vejstykkeEntity = this.getVejstykke();
        if (vejstykkeEntity != null) {
            obj.put("vej", vejstykkeEntity.toJSON());
            obj.put("kommune", vejstykkeEntity.getKommune().toJSON());
            if (!vejstykkeEntity.getLatestVersion().getPostnumre().isEmpty()) {
                JSONArray postnumre = new JSONArray();
                for (PostNummerEntity postNummerEntity : vejstykkeEntity.getLatestVersion().getPostnumre()) {
                    postnumre.put(postNummerEntity.toJSON());
                }
                obj.put("postnr", postnumre);
            }
        }
        if (!this.enhedsAdresseVersioner.isEmpty()) {
            JSONArray enhedsAdresser = new JSONArray();
            for (EnhedsAdresseVersionEntity enhedsAdresseVersionEntity : this.getEnhedsAdresseVersioner()) {
                if (enhedsAdresseVersionEntity.getEntity().getLatestVersion() == enhedsAdresseVersionEntity) {
                    enhedsAdresser.put(enhedsAdresseVersionEntity.getEntity().toJSON());
                }
            }
            obj.put("enhedsadresser", enhedsAdresser);
        }
        return obj;
    }
}
