package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
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
@Table(name = "dawa_enhedsadresse")
public class EnhedsAdresseEntity extends DobbeltHistorikBase<EnhedsAdresseEntity, EnhedsAdresseVersionEntity> {

    public EnhedsAdresseEntity() {
        this.versioner = new ArrayList<EnhedsAdresseVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Versioning */

    @OneToMany(mappedBy="entity")
    private Collection<EnhedsAdresseVersionEntity> versioner;

    @Override
    public Collection<EnhedsAdresseVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<EnhedsAdresseVersionEntity> versioner) {
        this.versioner = versioner;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EnhedsAdresseVersionEntity latestVersion;

    @Override
    public EnhedsAdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(EnhedsAdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private EnhedsAdresseVersionEntity preferredVersion;

    @Override
    public EnhedsAdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(EnhedsAdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected EnhedsAdresseVersionEntity createVersionEntity() {
        return new EnhedsAdresseVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "enhedsAdresse";
    }
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("id", this.getUuid());
        obj.put("etage", this.latestVersion.getEtage());
        obj.put("sidedoer", this.latestVersion.getDoer());
        obj.put("href", SearchService.getEnhedsAdresseBaseUrl()+"/"+this.getUuid());
        return obj;
    }
    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        AdgangsAdresseEntity adgangsAdresseEntity = this.latestVersion.getAdgangsadresse();
        if (adgangsAdresseEntity != null) {
            obj.put("husnr", adgangsAdresseEntity.toJSON());
            VejstykkeEntity vejstykkeEntity = adgangsAdresseEntity.getVejstykke();
            if (vejstykkeEntity != null) {
                obj.put("vej", vejstykkeEntity.toJSON());
                if (vejstykkeEntity.getKommune() != null) {
                    obj.put("kommune", vejstykkeEntity.getKommune().toJSON());
                }
            }
            if (adgangsAdresseEntity.getLatestVersion().getPostnummer() != null) {
                obj.put("postnr", adgangsAdresseEntity.getLatestVersion().getPostnummer().toJSON());
            } else if (adgangsAdresseEntity.getVejstykke().getLatestVersion().getPostnumre().size() == 1) {
                obj.put("postnr", adgangsAdresseEntity.getVejstykke().getLatestVersion().getPostnumre().iterator().next().toJSON());
            } else {
                JSONArray postnumre = new JSONArray();
                for (PostNummerEntity p : vejstykkeEntity.getLatestVersion().getPostnumre()) {
                    postnumre.put(p.toJSON());
                }
                obj.put("postnumre", postnumre);
            }
        }
        return obj;
    }
}
