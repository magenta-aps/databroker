package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.util.Util;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
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
        this.secondaryIndustries = new ArrayList<IndustryEntity>();
    }

    public CompanyUnitVersionEntity(CompanyUnitEntity entity) {
        super(entity);
        this.secondaryIndustries = new ArrayList<IndustryEntity>();
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


    @Column(nullable = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private Date addressDate;

    public Date getAddressDate() {
        return addressDate;
    }

    public void setAddressDate(Date addressDate) {
        this.addressDate = addressDate;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private EnhedsAdresseEntity address;

    public EnhedsAdresseEntity getAddress() {
        return address;
    }

    public void setAddress(EnhedsAdresseEntity address) {
        this.address = address;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private String fax;

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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


    @Column
    private boolean advertProtection;

    public boolean hasAdvertProtection() {
        return advertProtection;
    }

    public void setAdvertProtection(boolean advertProtection) {
        this.advertProtection = advertProtection;
    }

    //----------------------------------------------------

    public boolean matches(String name, EnhedsAdresseEntity address, Date addressDate,
                           IndustryEntity primaryIndustry, List<IndustryEntity> secondaryIndustries,
                           String phone, String fax, String email,
                           boolean advertProtection,
                           Date startDate, Date endDate) {
        return Util.compare(this.name, name) &&
                this.address.equals(address) &&
                Util.compare(this.addressDate, address) &&
                this.primaryIndustry.equals(primaryIndustry) &&
                Util.compare(this.secondaryIndustries, secondaryIndustries) &&
                Util.compare(this.phone, phone) &&
                Util.compare(this.fax, fax) &&
                Util.compare(this.email, email) &&
                Util.compare(this.advertProtection, advertProtection) &&
                Util.compare(this.startDate, startDate) &&
                Util.compare(this.endDate, endDate);
    }

}
