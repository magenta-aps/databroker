package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.stormodtagere.StormodtagerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.service.rest.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_adgangsadresse")
public class AdgangsAdresseEntity extends DobbeltHistorikBase<AdgangsAdresseEntity, AdgangsAdresseVersionEntity> {

    @OneToMany(mappedBy="entity")
    private Collection<AdgangsAdresseVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AdgangsAdresseVersionEntity latestVersion;

    @OneToOne
    private AdgangsAdresseVersionEntity preferredVersion;

    public AdgangsAdresseEntity() {
        this.versioner = new ArrayList<AdgangsAdresseVersionEntity>();
        this.enhedsAdresseVersioner = new ArrayList<EnhedsAdresseVersionEntity>();
        this.generateNewUUID();
    }

    @Override
    public Collection<AdgangsAdresseVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<AdgangsAdresseVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public AdgangsAdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdgangsAdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdgangsAdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdgangsAdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdgangsAdresseVersionEntity createVersionEntity() {
        return new AdgangsAdresseVersionEntity(this);
    }

    /* Domain specific fields */
    @OneToOne(mappedBy = "adgangsadresse")
    private StormodtagerEntity stormodtager;

    @OneToMany(mappedBy = "adgangsadresse")
    private Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner;

    public StormodtagerEntity getStormodtager() {
        return stormodtager;
    }

    public void setStormodtager(StormodtagerEntity stormodtager) {
        this.stormodtager = stormodtager;
    }

    public Collection<EnhedsAdresseVersionEntity> getEnhedsAdresseVersioner() {
        return enhedsAdresseVersioner;
    }

    public void setEnhedsAdresseVersioner(Collection<EnhedsAdresseVersionEntity> enhedsAdresseVersioner) {
        this.enhedsAdresseVersioner = enhedsAdresseVersioner;
    }

    public String getTypeName() {
        return "adgangsAdresse";
    }
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("husnr", this.latestVersion.getHusnr());
        obj.put("href", SearchService.getAdgangsAdresseBaseUrl()+"/"+this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        VejstykkeEntity vejstykkeEntity = this.latestVersion.getVejstykke();
        if (vejstykkeEntity != null) {
            obj.put("vej", vejstykkeEntity.toJSON());
            obj.put("kommune", vejstykkeEntity.getKommune().toJSON());
            PostNummerEntity postNummerEntity =  vejstykkeEntity.getLatestVersion().getPostnummer();
            if (postNummerEntity != null) {
                obj.put("postnr", postNummerEntity.toJSON());
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
