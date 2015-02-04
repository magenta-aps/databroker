package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
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
@Table(name = "cvr_companyunit")
public class CompanyUnitEntity extends DobbeltHistorikBase<CompanyUnitEntity, CompanyUnitVersionEntity> implements OutputFormattable, Cacheable {

    public CompanyUnitEntity() {
        this.versions = new ArrayList<CompanyUnitVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    private CompanyEntity company;

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }


    @Column
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
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
    @Index(name = "pno")
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
        JSONObject obj = super.toJSON();
        CompanyUnitVersionEntity latestVersion = this.latestVersion;
        obj.put("pNummer",this.pno);
        obj.put("advertProtection",latestVersion.hasAdvertProtection());
        obj.put("name",latestVersion.getName());
        obj.put("email",latestVersion.getEmail());
        obj.put("phone",latestVersion.getPhone());
        obj.put("fax",latestVersion.getFax());
        obj.put("startDate", latestVersion.getStartDate());
        obj.put("endDate", latestVersion.getEndDate());

        obj.put("address", latestVersion.getAddress().toJSON());
        obj.put("primaryIndustry", latestVersion.getPrimaryIndustry().toJSON());
        Collection<IndustryEntity> secondaryIndustries = latestVersion.getSecondaryIndustries();
        if (secondaryIndustries != null && !secondaryIndustries.isEmpty()) {
            JSONArray secondaryIndustryArray = new JSONArray();
            for (IndustryEntity secondaryIndustry : secondaryIndustries) {
                secondaryIndustryArray.put(secondaryIndustry.toJSON());
            }
            obj.put("secondaryIndustries", secondaryIndustryArray);
        }
        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "productionUnit";

    public static String joinEnhedsAdresse() {
        return databaseKey+".latestVersion.address as "+EnhedsAdresseEntity.databaseKey;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { ""+this.getPNO() };
    }
}
