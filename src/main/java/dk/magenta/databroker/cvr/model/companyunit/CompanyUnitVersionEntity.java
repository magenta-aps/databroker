package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerVersionEntity;
import dk.magenta.databroker.cvr.model.embeddable.CompanyInfo;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.util.Util;

import javax.persistence.*;
import java.util.Date;
import java.util.Collection;
import java.util.List;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "cvr_companyunit_version")
public class CompanyUnitVersionEntity extends DobbeltHistorikVersion<CompanyUnitEntity, CompanyUnitVersionEntity> {

    public CompanyUnitVersionEntity() {
        this.companyInfo = new CompanyInfo();
    }

    public CompanyUnitVersionEntity(CompanyUnitEntity entity) {
        super(entity);
        this.companyInfo = new CompanyInfo();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private CompanyUnitEntity entity;

    @Override
    public CompanyUnitEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(CompanyUnitEntity entity) {
        this.entity = entity;
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    //------------------------------------------------------

    private CompanyInfo companyInfo;

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    //------------------------------------------------------

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    private CompanyVersionEntity companyVersion;

    public CompanyVersionEntity getCompanyVersion() {
        return companyVersion;
    }

    public void setCompanyVersion(CompanyVersionEntity company) {
        this.companyVersion = company;
    }

    //------------------------------------------------------

    @Column
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
    }

    //------------------------------------------------------

    @OneToMany(mappedBy = "companyUnitVersion")
    private Collection<DeltagerVersionEntity> participants;

    public Collection<DeltagerVersionEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<DeltagerVersionEntity> participants) {
        this.participants = participants;
    }


    //----------------------------------------------------

    private boolean isPrimaryUnit;

    public boolean isPrimaryUnit() {
        return isPrimaryUnit;
    }

    public void setPrimaryUnit(boolean isPrimaryUnit) {
        this.isPrimaryUnit = isPrimaryUnit;
    }

    //----------------------------------------------------


    public boolean matches(String name, String cvrNummer, EnhedsAdresseEntity address, Date addressDate,
                           IndustryEntity primaryIndustry, List<IndustryEntity> secondaryIndustries,
                           String phone, String fax, String email, boolean isPrimaryUnit,
                           boolean advertProtection,
                           Date startDate, Date endDate) {
        return Util.compare(this.companyInfo.getName(), name) &&
                Util.compare(this.cvrNummer, cvrNummer) &&
                this.companyInfo.getLocationAddress().getEnhedsAdresse().equals(address) &&
                Util.compare(this.companyInfo.getLocationAddress().getValidFrom(), addressDate) &&
                this.companyInfo.getPrimaryIndustry().equals(primaryIndustry) &&
                Util.compare(this.companyInfo.getSecondaryIndustries(), secondaryIndustries) &&
                Util.compare(this.companyInfo.getTelephoneNumber(), phone) &&
                Util.compare(this.companyInfo.getTelefaxNumber(), fax) &&
                Util.compare(this.companyInfo.getEmail(), email) &&
                Util.compare(this.isPrimaryUnit, isPrimaryUnit) &&
                Util.compare(this.companyInfo.hasAdvertProtection(), advertProtection) &&
                Util.compare(this.companyInfo.getLifeCycle().getStartDate(), startDate) &&
                Util.compare(this.companyInfo.getLifeCycle().getEndDate(), endDate);
    }

}
