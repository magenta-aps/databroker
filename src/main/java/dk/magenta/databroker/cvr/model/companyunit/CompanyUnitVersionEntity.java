package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import org.hibernate.annotations.Index;

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
    }

    public CompanyUnitVersionEntity(CompanyUnitEntity entity) {
        super(entity);
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

    @ManyToOne(optional = false)
    @Index(name = "address")
    private EnhedsAdresseEntity address;

    public EnhedsAdresseEntity getAddress() {
        return address;
    }

    public void setAddress(EnhedsAdresseEntity address) {
        this.address = address;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    @Index(name = "phone")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    @Index(name = "fax")
    private String fax;

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    @Index(name = "email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @ManyToOne
    private EnhedsAdresseEntity adresse;

    public EnhedsAdresseEntity getAdresse() {
        return adresse;
    }

    public void setAdresse(EnhedsAdresseEntity adresse) {
        this.adresse = adresse;
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

    public boolean matches(String name, EnhedsAdresseEntity address,
                           IndustryEntity primaryIndustry, List<IndustryEntity> secondaryIndustries,
                           String phone, String fax, String email,
                           boolean advertProtection,
                           Date startDate, Date endDate) {
        return compare(this.name, name) &&
                this.address.equals(address) &&
                this.primaryIndustry.equals(primaryIndustry) &&
                compare(this.secondaryIndustries, secondaryIndustries) &&
                compare(this.phone, phone) &&
                compare(this.fax, fax) &&
                compare(this.email, email) &&
                this.advertProtection == advertProtection &&
                compare(this.startDate, startDate) &&
                compare(this.endDate, endDate);
    }


    private static boolean compare(String a, String b) {
        return a == null ? (b == null) : a.equals(b);
    }
    private static boolean compare(Object a, Object b) {
        return a == null ? (b == null) : a.equals(b);
    }
}
