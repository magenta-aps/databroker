package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cvr.model.embeddable.CompanyInfo;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.service.rest.SearchService;
import dk.magenta.databroker.util.cache.CacheableEntity;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 26-01-15.
 */

@Entity
@Table(name = "cvr_companyunit")
public class CompanyUnitEntity extends DobbeltHistorikBase<CompanyUnitEntity, CompanyUnitVersionEntity> implements OutputFormattable, CacheableEntity {

    public CompanyUnitEntity() {
        this.versions = new ArrayList<CompanyUnitVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompanyUnitVersionEntity> versions;

    @Override
    public Collection<CompanyUnitVersionEntity> getVersions() {
        return versions;
    }

    public void setVersions(Collection<CompanyUnitVersionEntity> versions) {
        this.versions = versions;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CompanyUnitVersionEntity latestVersion;

    @Override
    public CompanyUnitVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CompanyUnitVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private CompanyUnitVersionEntity preferredVersion;

    @Override
    public CompanyUnitVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CompanyUnitVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected CompanyUnitVersionEntity createVersionEntity() {
        return new CompanyUnitVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column(nullable = false)
    @Index(name = "pnoIndex")
    private long pno;

    public long getPNO() {
        return pno;
    }

    public void setPNO(long pno) {
        this.pno = pno;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "companyunit";
    }

    public JSONObject toJSON() {
        return this.toJSON(this.latestVersion);
    }
    public JSONObject toFullJSON() {
        return this.toFullJSON(this.latestVersion);
    }

    public JSONObject toJSON(CompanyUnitVersionEntity version) {
        JSONObject obj = super.toJSON();
        obj.put("pNummer",this.pno);
        obj.put("href", SearchService.getCompanyUnitBaseUrl() + "/" + this.getPNO());
        version.getCompanyInfo().addToJSONObject(obj);
        return obj;
    }
    public JSONObject toFullJSON(CompanyUnitVersionEntity version) {
        JSONObject obj = this.toJSON(version);
        try {
            obj.put("company", version.getCompanyVersion().getEntity().toJSON());
        } catch (NullPointerException e) {
            System.err.println("NPE on "+this.pno);
        }
        return obj;
    }
    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "productionUnit";
    private static final String companyInfoKey = databaseKey+".latestVersion.companyInfo";

    public static String joinEnhedsAdresse() {
        return databaseKey+".latestVersion.companyInfo.locationAddress.enhedsAdresse as "+EnhedsAdresseEntity.databaseKey;
    }

    public static Condition pnoCondition(long pno) {
        return RepositoryUtil.whereField(pno, databaseKey + ".pno", null);
    }
    public static Condition pnoCondition(SearchParameters parameters) {
        if (parameters.has(SearchParameters.Key.PNR)) {
            return RepositoryUtil.whereField(parameters.get(SearchParameters.Key.PNR), databaseKey + ".pno", null);
        }
        return null;
    }

    public static Condition cvrCondition(SearchParameters parameters) {
        if (parameters.has(SearchParameters.Key.CVR)) {
            return RepositoryUtil.whereField(parameters.get(SearchParameters.Key.CVR), null, databaseKey+".cvrNummer");
        }
        return null;
    }
    public static Condition cvrCondition(String cvrNummer) {
        return RepositoryUtil.whereField(cvrNummer, null, databaseKey+".cvrNummer");
    }
    public static Condition nameCondition(SearchParameters parameters) {
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
    public static Condition landCondition(SearchParameters parameters) {
        return CompanyInfo.landCondition(parameters, companyInfoKey);
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

    @Override
    public String[] getIdentifiers() {
        return new String[] { ""+this.getPNO() };
    }


}
