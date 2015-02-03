package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.stormodtagere.StormodtagerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
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
        this.enhedsAdresser = new ArrayList<EnhedsAdresseEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Versioning */

    @OneToMany(mappedBy="entity")
    private Collection<AdgangsAdresseVersionEntity> versioner;

    @Override
    public Collection<AdgangsAdresseVersionEntity> getVersions() {
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
    private Collection<EnhedsAdresseEntity> enhedsAdresser;

    public Collection<EnhedsAdresseEntity> getEnhedsAdresser() {
        return enhedsAdresser;
    }

    public void setEnhedsAdresser(Collection<EnhedsAdresseEntity> enhedsAdresser) {
        this.enhedsAdresser = enhedsAdresser;
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

    public int getIntHusnr() {
        try {
            return Integer.parseInt(this.husnr.replaceAll("[^\\d]",""), 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Column(nullable = true, length = 6)
    private String bnr;

    public String getBnr() {
        return bnr;
    }

    public void setBnr(String bnr) {
        this.bnr = bnr;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "adgangsAdresse";
    }
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("id", this.getUuid());
        obj.put("husnummer", this.getHusnr());
        obj.put("href", SearchService.getAdgangsAdresseBaseUrl()+"/"+this.getUuid());
        if (this.bnr != null) {
            obj.put("b-nummer", this.bnr);
        }
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        VejstykkeEntity vejstykkeEntity = this.getVejstykke();
        if (vejstykkeEntity != null) {
            obj.put("vej", vejstykkeEntity.toJSON());
            obj.put("kommune", vejstykkeEntity.getKommune().toJSON());
        }
        if (this.latestVersion.getPostnummer() != null) {
            obj.put("postnr", this.latestVersion.getPostnummer().toJSON());
        } else if (this.getVejstykke().getLatestVersion().getPostnumre().size() == 1) {
            obj.put("postnr", this.getVejstykke().getLatestVersion().getPostnumre().iterator().next().toJSON());
        } else {
            JSONArray postnumre = new JSONArray();
            for (PostNummerEntity p : vejstykkeEntity.getLatestVersion().getPostnumre()) {
                postnumre.put(p.toJSON());
            }
            obj.put("postnumre", postnumre);
        }

        if (!this.enhedsAdresser.isEmpty()) {
            JSONArray enhedsAdresser = new JSONArray();
            for (EnhedsAdresseEntity enhedsAdresseEntity : this.getEnhedsAdresser()) {
                enhedsAdresser.put(enhedsAdresseEntity.toJSON());
            }
            obj.put("enhedsadresser", enhedsAdresser);
        }
        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "adgang";
    public static Condition bnrCondition(SearchParameters parameters) {
        if (parameters.has(Key.BNR)) {
            return RepositoryUtil.whereField(parameters.get(Key.BNR), null, databaseKey + ".bnr");
        }
        return null;
    }
    public static Condition husnrCondition(SearchParameters parameters) {
        if (parameters.has(Key.HUSNR)) {
            return RepositoryUtil.whereField(parameters.get(Key.HUSNR), null, databaseKey+".husnr");
        }
        return null;
    }
    public static String joinVej() {
        return databaseKey+".vejstykke as "+VejstykkeEntity.databaseKey;
    }
}
