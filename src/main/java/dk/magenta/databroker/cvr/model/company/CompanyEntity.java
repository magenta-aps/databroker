package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
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


    @OneToMany(mappedBy = "company")
    private Collection<CompanyUnitEntity> units;

    public Collection<CompanyUnitEntity> getUnits() {
        return this.units;
    }

    public void setUnits(Collection<CompanyUnitEntity> units) {
        this.units = units;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "company";
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("name", this.latestVersion.getName());
        obj.put("cvrnr", this.getCvrNummer());
        obj.put("href", SearchService.getCompanyBaseUrl() + "/" + this.getCvrNummer());
        return obj;
    }

    @Override
    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        Collection<CompanyUnitEntity> units = this.units;
        if (units != null && !units.isEmpty()) {
            JSONArray unitArray = new JSONArray();
            for (CompanyUnitEntity unit : units) {
                unitArray.put(unit.toJSON());
            }
            obj.put("units", unitArray);
        }
        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "company";

    public static Condition cvrCondition(SearchParameters parameters) {
        if (parameters.has(Key.CVR)) {
            return RepositoryUtil.whereField(parameters.get(Key.CVR), null, databaseKey+".cvrNummer");
        }
        return null;
    }
    public static Condition virksomhedCondition(SearchParameters parameters) {
        if (parameters.has(Key.VIRKSOMHED)) {
            return RepositoryUtil.whereField(parameters.get(Key.VIRKSOMHED), null, databaseKey+".latestVersion.name");
        }
        return null;
    }

    public static String joinCompanyUnit() {
        return databaseKey+".units as "+CompanyUnitEntity.databaseKey;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { this.getCvrNummer() };
    }
}