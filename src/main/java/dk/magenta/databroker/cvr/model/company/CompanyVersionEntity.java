package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.cvr.model.company.companydeltagere.CompanyDeltagerRelationEntity;
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
import java.util.*;

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
        this.companyDeltagerRelationEntities = new ArrayList<CompanyDeltagerRelationEntity>();
    }

    public CompanyVersionEntity(CompanyEntity entity) {
        super(entity);
        this.unitVersions = new ArrayList<CompanyUnitVersionEntity>();
        this.companyInfo = new CompanyInfo();
        this.creditInformation = new ValidFromField();
        this.participants = new ArrayList<DeltagerVersionEntity>();
        this.companyDeltagerRelationEntities = new ArrayList<CompanyDeltagerRelationEntity>();
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






    @OneToMany(mappedBy = "companyVersionEntity", cascade = CascadeType.ALL)
    private Collection<CompanyDeltagerRelationEntity> companyDeltagerRelationEntities;

    public Collection<CompanyDeltagerRelationEntity> getCompanyDeltagerRelationEntities() {
        return companyDeltagerRelationEntities;
    }

    public void addCompanyDeltagerRelationEntitiy(CompanyDeltagerRelationEntity companyDeltagerRelationEntity) {
        this.companyDeltagerRelationEntities.add(companyDeltagerRelationEntity);
    }

    private Map<Long, Date> getSimplifiedDeltagerList() {
        HashMap<Long, Date> deltagere = new HashMap<Long, Date>();
        for (CompanyDeltagerRelationEntity companyDeltagerRelationEntity : this.companyDeltagerRelationEntities) {
            deltagere.put(companyDeltagerRelationEntity.getDeltagerNummer(), new Date(companyDeltagerRelationEntity.getValidFrom().getTime()));
        }
        return deltagere.isEmpty() ? null : deltagere;
    }

    //----------------------------------------------------

    public boolean matches(CompanyFormEntity form, CompanyInfo companyInfo, Map<Long, Date> deltagere) {
        if (deltagere != null && deltagere.isEmpty()) {
            deltagere = null;
        }
        return Util.compare(this.form, form) &&
               Util.compare(this.getCompanyInfo(), companyInfo) &&
                Util.compare(this.getSimplifiedDeltagerList(), deltagere);
    }


}
