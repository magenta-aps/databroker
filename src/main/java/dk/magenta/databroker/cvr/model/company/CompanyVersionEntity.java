package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.cvr.model.form.FormEntity;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
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

    @OneToMany(mappedBy = "company")
    private Collection<CompanyUnitEntity> units;

    public Collection<CompanyUnitEntity> getCompanyUnits() {
        return units;
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

    @Column(length = 25)
    @Index(name = "primaryAddressDescriptorIndex")
    private long primaryAddressDescriptor;

    public long getPrimaryAddressDescriptor() {
        return this.primaryAddressDescriptor;
    }

    public void setPrimaryAddressDescriptor(long primaryAddressDescriptor) {
        this.primaryAddressDescriptor = primaryAddressDescriptor;
    }

    //----------------------------------------------------

    @Column
    @Index(name = "nameIndex")
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

    @ManyToOne
    private FormEntity form;

    public FormEntity getForm() {
        return form;
    }

    public void setForm(FormEntity form) {
        this.form = form;
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

    @Column(nullable = true)
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

    public boolean matches(String name, FormEntity form, IndustryEntity primaryIndustry, Set<IndustryEntity> secondaryIndustries, Date startDate, Date endDate, long primaryAddressDescriptor) {
        return Util.compare(this.name, name) &&
                Util.compare(this.form, form) &&
                Util.compare(this.primaryIndustry, primaryIndustry) &&
                Util.compare(this.secondaryIndustries, secondaryIndustries) &&
                Util.compare(this.units, units) &&
                Util.compare(this.startDate, startDate) &&
                Util.compare(this.endDate, endDate) &&
                Util.compare(this.primaryAddressDescriptor, primaryAddressDescriptor);
    }


}
