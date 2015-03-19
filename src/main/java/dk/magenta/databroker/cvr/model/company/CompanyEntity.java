package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cvr.model.company.companydeltagere.CompanyDeltagerRelationEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerVersionEntity;
import dk.magenta.databroker.cvr.model.embeddable.CompanyInfo;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.service.rest.SearchService;
import dk.magenta.databroker.util.cache.Cacheable;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 26-01-15.
 */

@Entity
@Table(name = "cvr_company")
public class CompanyEntity extends DobbeltHistorikBase<CompanyEntity, CompanyVersionEntity> implements OutputFormattable, Cacheable {

    public CompanyEntity() {
        this.versions = new ArrayList<CompanyVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompanyVersionEntity> versions;

    @Override
    public Collection<CompanyVersionEntity> getVersions() {
        return versions;
    }

    public void setVersions(Collection<CompanyVersionEntity> versions) {
        this.versions = versions;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CompanyVersionEntity latestVersion;

    @Override
    public CompanyVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CompanyVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private CompanyVersionEntity preferredVersion;

    @Override
    public CompanyVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CompanyVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected CompanyVersionEntity createVersionEntity() {
        return new CompanyVersionEntity(this);
    }


    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column(nullable = false)
    @Index(name = "cvrNummerIndex")
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "company";
    }

    @Override
    public JSONObject toJSON() {
        return this.toJSON(this.latestVersion);
    }

    public JSONObject toJSON(CompanyVersionEntity version) {
        JSONObject obj = super.toJSON();
        obj.put("cvrnr", this.getCvrNummer());
        obj.put("href", SearchService.getCompanyBaseUrl() + "/" + this.getCvrNummer());
        obj.put("companyform", version.getForm().toJSON());

        JSONArray deltagere = new JSONArray();
        for (CompanyDeltagerRelationEntity rel : version.getCompanyDeltagerRelationEntities()) {
            JSONObject deltager = new JSONObject();
            deltager.put("deltagerNummer", rel.getDeltagerNummer());
            deltager.put("validFrom", rel.getValidFrom());
            deltagere.put(deltager);
        }
        obj.put("deltagere", deltagere);
        version.getCompanyInfo().addToJSONObject(obj);
        return obj;
    }

    @Override
    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        Collection<CompanyUnitVersionEntity> units = this.latestVersion.getUnitVersions();
        if (units != null && !units.isEmpty()) {
            JSONArray unitArray = new JSONArray();
            for (CompanyUnitVersionEntity unit : units) {
                unitArray.put(unit.getEntity().toJSON(unit));
            }
            obj.put("units", unitArray);
        }
        Collection<DeltagerVersionEntity> participants = this.latestVersion.getParticipants();
        if (participants != null && !participants.isEmpty()) {
            JSONArray participantArray = new JSONArray();
            for (DeltagerVersionEntity participant : participants) {
                participantArray.put(participant.getEntity().toJSON(participant));
            }
            obj.put("participants", participantArray);
        }
        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "company";
    private static final String companyInfoKey = databaseKey+".latestVersion.companyInfo";

    public static Condition cvrCondition(SearchParameters parameters) {
        if (parameters.has(Key.CVR)) {
            return RepositoryUtil.whereField(parameters.get(Key.CVR), null, databaseKey+".cvrNummer");
        }
        return null;
    }
    public static Condition cvrCondition(String cvrNummer) {
        return RepositoryUtil.whereField(cvrNummer, null, databaseKey+".cvrNummer");
    }
    public static Condition virksomhedCondition(SearchParameters parameters) {
        return CompanyInfo.nameCondition(parameters, companyInfoKey);
    }
    public static Condition emailCondition(SearchParameters parameters) {
        return CompanyInfo.emailCondition(parameters, companyInfoKey);
    }
    public static Condition phoneCondition(SearchParameters parameters) {
        return CompanyInfo.phoneCondition(parameters, companyInfoKey);
    }
    public static Condition faxCondition(SearchParameters parameters) {
        return CompanyInfo.faxCondition(parameters, companyInfoKey);
    }
    public static Condition primaryIndustryCondition(SearchParameters parameters) {
        return CompanyInfo.primaryIndustryCondition(parameters, companyInfoKey);
    }
    public static Condition secondaryIndustryCondition(SearchParameters parameters) {
        return CompanyInfo.secondaryIndustryCondition(parameters, companyInfoKey);
    }
    public static Condition anyIndustryCondition(SearchParameters parameters) {
        return CompanyInfo.anyIndustryCondition(parameters, companyInfoKey);
    }
    public static Condition kommuneCondition(SearchParameters parameters) {
        return CompanyInfo.kommuneCondition(parameters, companyInfoKey);
    }
    public static Condition vejCondition(SearchParameters parameters) {
        return CompanyInfo.vejCondition(parameters, companyInfoKey);
    }
    public static Condition postCondition(SearchParameters parameters) {
        return CompanyInfo.postCondition(parameters, companyInfoKey);
    }
    public static Condition lokalitetCondition(SearchParameters parameters) {
        return CompanyInfo.lokalitetCondition(parameters, companyInfoKey);
    }
    public static Condition husnrCondition(SearchParameters parameters) {
        return CompanyInfo.husnrCondition(parameters, companyInfoKey);
    }
    public static Condition etageCondition(SearchParameters parameters) {
        return CompanyInfo.etageCondition(parameters, companyInfoKey);
    }
    public static Condition doerCondition(SearchParameters parameters) {
        return CompanyInfo.doerCondition(parameters, companyInfoKey);
    }

    public static String[] joinCompanyUnit() {
        String versionsKey = databaseKey+"_versions";
        String unitVersionsKey = CompanyUnitEntity.databaseKey+"_versions";
        return new String[] {
            databaseKey + ".versions as " + versionsKey,
                versionsKey + ".unitVersions as " + unitVersionsKey,
                unitVersionsKey + ".entity as " + CompanyUnitEntity.databaseKey
        };
    }

    //public static Condition

    @Override
    public String[] getIdentifiers() {
        return new String[] { this.getCvrNummer() };
    }
}
