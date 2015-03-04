package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerVersionEntity;
import dk.magenta.databroker.cvr.model.embeddable.*;
import dk.magenta.databroker.cvr.model.form.CompanyFormEntity;
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
@org.hibernate.annotations.Table(
        appliesTo = "cvr_company_version",
        indexes = {
                @Index(name = "locationAddressDescriptorIndex", columnNames = {"locationAddressDescriptor"}),
                @Index(name = "postalAddressDescriptorIndex", columnNames = {"postalAddressDescriptor"}),
        }
)
public class CompanyVersionEntity extends DobbeltHistorikVersion<CompanyEntity, CompanyVersionEntity> {

    public CompanyVersionEntity() {
        this.unitVersions = new ArrayList<CompanyUnitVersionEntity>();
        this.companyInfo = new CompanyInfo();
        this.creditInformation = new ValidFromField();
        this.participants = new ArrayList<DeltagerVersionEntity>();
    }

    public CompanyVersionEntity(CompanyEntity entity) {
        super(entity);
        this.unitVersions = new ArrayList<CompanyUnitVersionEntity>();
        this.companyInfo = new CompanyInfo();
        this.creditInformation = new ValidFromField();
        this.participants = new ArrayList<DeltagerVersionEntity>();
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

    @OneToMany(mappedBy = "companyVersion")
    private Collection<CompanyUnitVersionEntity> unitVersions;

    public Collection<CompanyUnitVersionEntity> getUnitVersions() {
        return unitVersions;
    }

    public void setUnitVersions(Collection<CompanyUnitVersionEntity> unitVersions) {
        this.unitVersions = unitVersions;
    }

    //----------------------------------------------------

    @OneToOne(optional = true)
    private CompanyUnitEntity primaryUnit;

    public CompanyUnitEntity getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(CompanyUnitEntity primaryUnit) {
        if (this.unitVersions.contains(primaryUnit)) {
            this.primaryUnit = primaryUnit;
        }
    }

    //----------------------------------------------------

    private CompanyInfo companyInfo;

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    //----------------------------------------------------

    @ManyToOne
    private CompanyFormEntity form;

    public CompanyFormEntity getForm() {
        return form;
    }

    public void setForm(CompanyFormEntity form) {
        this.form = form;
    }

    //----------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "validFrom", column = @Column(name = "creditInformationValidFrom")),
            @AttributeOverride(name = "text", column = @Column(name = "creditInformation"))
    })
    private ValidFromField creditInformation;

    public ValidFromField getCreditInformation() {
        return creditInformation;
    }

    public void setCreditInformation(ValidFromField creditInformation) {
        this.creditInformation = creditInformation;
    }

    //----------------------------------------------------

    @OneToMany(mappedBy = "companyVersion")
    private Collection<DeltagerVersionEntity> participants;

    public Collection<DeltagerVersionEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<DeltagerVersionEntity> participants) {
        this.participants = participants;
    }

    //----------------------------------------------------

    public boolean matches(
            String name,
            CompanyFormEntity form,
            IndustryEntity primaryIndustry,
            Set<IndustryEntity> secondaryIndustries,
            Date startDate,
            Date endDate,
            String primaryAddressDescriptor
    ) {
        CompanyInfo cInfo = this.getCompanyInfo();
        return Util.compare(cInfo.getName(), name) &&
                Util.compare(this.form, form) &&
                Util.compare(this.companyInfo.getPrimaryIndustry(), primaryIndustry) &&
                Util.compare(this.companyInfo.getSecondaryIndustries(), secondaryIndustries) &&
                Util.compare(this.unitVersions, unitVersions) &&
                Util.compare(this.companyInfo.getLifeCycle().getStartDate(), startDate) &&
                Util.compare(this.companyInfo.getLifeCycle().getEndDate(), endDate) &&
                // TODO: Compare both location and postal address?
                Util.compare(this.companyInfo.getLocationAddress().getDescriptor(), primaryAddressDescriptor);
    }


}
