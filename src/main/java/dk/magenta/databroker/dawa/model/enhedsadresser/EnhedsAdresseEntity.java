package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.service.rest.SearchService;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.cache.CacheableEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_enhedsadresse", indexes = {@javax.persistence.Index(columnList = "descriptor")})
public class EnhedsAdresseEntity extends DobbeltHistorikBase<EnhedsAdresseEntity, EnhedsAdresseVersionEntity> implements CacheableEntity {

    public EnhedsAdresseEntity() {
        this.versioner = new ArrayList<EnhedsAdresseVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Versioning */

    @OneToMany(mappedBy="entity")
    private Collection<EnhedsAdresseVersionEntity> versioner;

    @Override
    public Collection<EnhedsAdresseVersionEntity> getVersions() {
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

    @ManyToOne(optional = false)
    private AdgangsAdresseEntity adgangsadresse;

    public AdgangsAdresseEntity getAdgangsadresse() {
        return adgangsadresse;
    }

    public void setAdgangsadresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsadresse = adgangsAdresse;
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
    //----------------------------------------------------

    @Column
    private String descriptor;

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static String generateDescriptor(int kommuneKode, int vejKode, int husNr, Character husbogstav, String etage, String doer) {
        String fullHusNr = String.valueOf((husNr > 0) ? husNr : "") + (husbogstav != null ? husbogstav : "");
        return generateDescriptor(kommuneKode, vejKode, fullHusNr, etage, doer);
    }
    public static String generateDescriptor(int kommuneKode, int vejKode, int husNr, char husbogstav, String etage, String doer) {
        String fullHusNr = String.valueOf((husNr > 0) ? husNr : "") + husbogstav;
        return generateDescriptor(kommuneKode, vejKode, fullHusNr, etage, doer);
    }
    public static String generateDescriptor(int kommuneKode, int vejKode, String husNr, String etage, String doer) {
        return kommuneKode+":"+vejKode+":"+Util.emptyIfNull(husNr)+":"+Util.emptyIfNull(etage)+":"+Util.emptyIfNull(doer);
    }

    public void generateDescriptor() {
        AdgangsAdresseEntity adgangsAdresseEntity = this.getAdgangsadresse();
        VejstykkeEntity vejstykkeEntity = adgangsAdresseEntity.getVejstykke();
        KommuneEntity kommuneEntity = vejstykkeEntity.getKommune();
        this.setDescriptor(generateDescriptor(kommuneEntity.getKode(), vejstykkeEntity.getKode(), adgangsAdresseEntity.getHusnr(), this.getEtage(), this.getDoer()));
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "enhedsAdresse";
    }
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("id", this.getUuid());
        obj.put("etage", this.getEtage());
        obj.put("sidedoer", this.getDoer());
        if (this.latestVersion.getKaldenavn() != null) {
            obj.put("kaldenavn", this.latestVersion.getKaldenavn());
        }
        obj.put("href", SearchService.getEnhedsAdresseBaseUrl()+"/"+this.getUuid());
        return obj;
    }
    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        AdgangsAdresseEntity adgangsAdresseEntity = this.getAdgangsadresse();
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

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "adresse";
    public static Condition etageCondition(SearchParameters parameters) {
        if (parameters.has(Key.ETAGE)) {
            return RepositoryUtil.whereField(parameters.get(Key.ETAGE), null, databaseKey+".etage");
        }
        return null;
    }
    public static Condition doerCondition(SearchParameters parameters) {
        if (parameters.has(Key.DOER)) {
            return RepositoryUtil.whereField(parameters.get(Key.DOER), null, databaseKey+".doer");
        }
        return null;
    }
    public static Condition descriptorCondition(String descriptor) {
        return RepositoryUtil.whereField(descriptor, null, databaseKey+".descriptor");
    }
    public static String joinAdgangsAdresse() {
        return databaseKey+".adgangsadresse as " + AdgangsAdresseEntity.databaseKey;
    }

    @Override
    public String[] getIdentifiers() {
        AdgangsAdresseEntity adgangsAdresseEntity = this.getAdgangsadresse();
        VejstykkeEntity vejstykkeEntity = adgangsAdresseEntity.getVejstykke();
        KommuneEntity kommuneEntity = vejstykkeEntity.getKommune();
        return new String[] { ""+kommuneEntity.getKode(), ""+vejstykkeEntity.getKode(), adgangsAdresseEntity.getHusnr(), getFinalIdentifier(this.getEtage(), this.getDoer()) };
    }

    public static String getFinalIdentifier(String etage, String doer) {
        return Util.emptyIfNull(etage)+":"+Util.emptyIfNull(doer);
    }
}
