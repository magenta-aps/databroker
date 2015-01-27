package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.Util;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "cvr_company_version")
public class CompanyVersionEntity extends DobbeltHistorikVersion<CompanyEntity, CompanyVersionEntity> {

    public CompanyVersionEntity() {
        this.units = new ArrayList<CompanyUnitEntity>();
        this.secondaryIndustries = new ArrayList<IndustryEntity>();
    }

    public CompanyVersionEntity(CompanyEntity entity) {
        super(entity);
        this.units = new ArrayList<CompanyUnitEntity>();
        this.secondaryIndustries = new ArrayList<IndustryEntity>();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private CompanyEntity entity;

    @Override
    public CompanyEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(CompanyEntity entity) {
        this.entity = entity;
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @OneToMany
    private Collection<CompanyUnitEntity> units;

    public Collection<CompanyUnitEntity> getCompanyUnits() {
        return units;
    }

    public void addUnit(CompanyUnitEntity unit) {
        if (!this.units.contains(unit)) {
            this.units.add(unit);
        }
    }
    public void removeUnit(CompanyUnitEntity unit) {
        if (this.units.contains(unit)) {
            this.units.remove(unit);
        }
    }

    //----------------------------------------------------

    @OneToOne(optional = true)
    private CompanyUnitEntity primaryUnit;

    public CompanyUnitEntity getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(CompanyUnitEntity primaryUnit) {
        if (this.units.contains(primaryUnit)) {
            this.primaryUnit = primaryUnit;
        }
    }

    //----------------------------------------------------

    @Column(nullable = false)
    @Index(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    @ManyToOne
    private IndustryEntity primaryIndustry;

    public IndustryEntity getPrimaryIndustry() {
        return primaryIndustry;
    }

    public void setPrimaryIndustry(IndustryEntity primaryIndustry) {
        this.primaryIndustry = primaryIndustry;
    }

    //----------------------------------------------------

    @ManyToMany
    private Collection<IndustryEntity> secondaryIndustries;

    public Collection<IndustryEntity> getSecondaryIndustries() {
        return secondaryIndustries;
    }

    public void addSecondaryIndustry(IndustryEntity secondaryIndustry) {
        if (!this.hasSecondaryIndustry(secondaryIndustry)) {
            this.secondaryIndustries.add(secondaryIndustry);
        }
    }
    public void removeSecondaryIndustry(IndustryEntity secondaryIndustry) {
        if (this.hasSecondaryIndustry(secondaryIndustry)) {
            this.secondaryIndustries.remove(secondaryIndustry);
        }
    }
    public boolean hasSecondaryIndustry(IndustryEntity secondaryIndustry) {
        return this.secondaryIndustries.contains(secondaryIndustry);
    }

    //----------------------------------------------------

    @Column(nullable = false)
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    //----------------------------------------------------

    public boolean matches(String name, IndustryEntity primaryIndustry, Set<IndustryEntity> secondaryIndustries,
                           CompanyUnitEntity primaryUnit, Set<CompanyUnitEntity> units, Date startDate, Date endDate) {
        return Util.compare(this.name, name) &&
                Util.compare(this.primaryIndustry, primaryIndustry) &&
                Util.compare(this.secondaryIndustries, secondaryIndustries) &&
                Util.compare(this.primaryUnit, primaryUnit) &&
                Util.compare(this.units, units) &&
                Util.compare(this.startDate, startDate) &&
                Util.compare(this.endDate, endDate);
    }


}
