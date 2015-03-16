package dk.magenta.databroker.cvr.model.embeddable;

import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by jubk on 04-03-2015.
 */
@Embeddable
public class CompanyInfo {

    @Embedded
    private LifeCycle lifeCycle;

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    //-----------------------------------------------------------

    @Column(nullable = true)
    private Date updateDate;

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    //-----------------------------------------------------------

    @Column
    private boolean advertProtection;

    public boolean hasAdvertProtection() {
        return advertProtection;
    }

    public void setAdvertProtection(boolean advertProtection) {
        this.advertProtection = advertProtection;
    }

    //-----------------------------------------------------------

    @Column(nullable = true)
    @Index(name = "nameIndex")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="validFrom", column = @Column(name = "locationAddressValidFrom")),
            @AttributeOverride(name="roadName", column = @Column(name = "locationAddressRoadName")),
            @AttributeOverride(name="roadCode", column = @Column(name = "locationAddressRoadCode")),
            @AttributeOverride(name="housenumberFrom", column = @Column(name = "locationAddressHouseNumberFrom")),
            @AttributeOverride(name="housenumberTo", column = @Column(name = "locationAddressHouseNumberTo")),
            @AttributeOverride(name="letterFrom", column = @Column(name = "locationAddressLetterFrom")),
            @AttributeOverride(name="letterTo", column = @Column(name = "locationAddressLetterTo")),
            @AttributeOverride(name="floor", column = @Column(name = "locationAddressFloor")),
            @AttributeOverride(name="sideOrDoor", column = @Column(name = "locationAddressSideOrDoor")),
            @AttributeOverride(name="postalCode", column = @Column(name = "locationAddressPostalCode")),
            @AttributeOverride(name="postalDistrict", column = @Column(name = "locationAddressPostalDistrict")),
            @AttributeOverride(name="cityName", column = @Column(name = "locationAddressCityName")),
            @AttributeOverride(name="municipalityCode", column = @Column(name = "locationAddressMunicipalityCode")),
            @AttributeOverride(name="municipalityText", column = @Column(name = "locationAddressMunicipalityText")),
            @AttributeOverride(name="postBox", column = @Column(name = "locationAddressPostbox")),
            @AttributeOverride(name="coName", column = @Column(name = "locationAddressCoName")),
            @AttributeOverride(name="freetextAddress", column = @Column(name = "locationAddressFreetext")),
            @AttributeOverride(name="descriptor", column = @Column(name = "locationAddressDescriptor")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="enhedsAdresse", joinColumns = @JoinColumn(name = "locationAddressEnhedsAdresse")),

    })
    private CvrAddress locationAddress;

    public CvrAddress getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(CvrAddress locationAddress) {
        this.locationAddress = locationAddress;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="validFrom", column = @Column(name = "postalAddressValidFrom")),
            @AttributeOverride(name="roadName", column = @Column(name = "postalAddressRoadName")),
            @AttributeOverride(name="roadCode", column = @Column(name = "postalAddressRoadCode")),
            @AttributeOverride(name="housenumberFrom", column = @Column(name = "postalAddressHouseNumberFrom")),
            @AttributeOverride(name="housenumberTo", column = @Column(name = "postalAddressHouseNumberTo")),
            @AttributeOverride(name="letterFrom", column = @Column(name = "postalAddressLetterFrom")),
            @AttributeOverride(name="letterTo", column = @Column(name = "postalAddressLetterTo")),
            @AttributeOverride(name="floor", column = @Column(name = "postalAddressFloor")),
            @AttributeOverride(name="sideOrDoor", column = @Column(name = "postalAddressSideOrDoor")),
            @AttributeOverride(name="postalCode", column = @Column(name = "postalAddressPostalCode")),
            @AttributeOverride(name="postalDistrict", column = @Column(name = "postalAddressPostalDistrict")),
            @AttributeOverride(name="cityName", column = @Column(name = "postalAddressCityName")),
            @AttributeOverride(name="municipalityCode", column = @Column(name = "postalAddressMunicipalityCode")),
            @AttributeOverride(name="municipalityText", column = @Column(name = "postalAddressMunicipalityText")),
            @AttributeOverride(name="postBox", column = @Column(name = "postalAddressPostbox")),
            @AttributeOverride(name="coName", column = @Column(name = "postalAddressCoName")),
            @AttributeOverride(name="freetextAddress", column = @Column(name = "postalAddressFreetext")),
            @AttributeOverride(name="descriptor", column = @Column(name = "postalAddressDescriptor")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="enhedsAdresse", joinColumns = @JoinColumn(name = "postalAddressEnhedsAdresse")),

    })
    private CvrAddress postalAddress;

    public CvrAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(CvrAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "validFrom", column = @Column(name = "telephoneNumberValidFrom")),
            @AttributeOverride(name = "text", column = @Column(name = "telephoneNumber")),
    })
    private ValidFromField telephoneNumber;

    public ValidFromField getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(ValidFromField telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "validFrom", column = @Column(name = "telefaxNumberValidFrom")),
            @AttributeOverride(name = "text", column = @Column(name = "telefaxNumber"))
    })
    private ValidFromField telefaxNumber;

    public ValidFromField getTelefaxNumber() {
        return telefaxNumber;
    }

    public void setTelefaxNumber(ValidFromField telefaxNumber) {
        this.telefaxNumber = telefaxNumber;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "validFrom", column = @Column(name = "emailValidFrom")),
            @AttributeOverride(name = "text", column = @Column(name = "email"))
    })
    private ValidFromField email;

    public ValidFromField getEmail() {
        return email;
    }

    public void setEmail(ValidFromField email) {
        this.email = email;
    }

    public String getEmailText() {
        return this.email != null ? this.email.getText() : null;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
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

    public void setSecondaryIndustries(Collection<IndustryEntity> secondaryIndustries) {
        this.secondaryIndustries = secondaryIndustries;
    }

    //----------------------------------------------------

    private YearlyEmployeeNumbers yearlyEmployeeNumbers;

    public YearlyEmployeeNumbers getYearlyEmployeeNumbers() {
        return yearlyEmployeeNumbers;
    }

    public void setYearlyEmployeeNumbers(YearlyEmployeeNumbers yearlyEmployeeNumbers) {
        this.yearlyEmployeeNumbers = yearlyEmployeeNumbers;
    }

    //----------------------------------------------------

    private QuarterlyEmployeeNumbers quarterlyEmployeeNumbers;

    public QuarterlyEmployeeNumbers getQuarterlyEmployeeNumbers() {
        return quarterlyEmployeeNumbers;
    }

    public void setQuarterlyEmployeeNumbers(QuarterlyEmployeeNumbers quarterlyEmployeeNumbers) {
        this.quarterlyEmployeeNumbers = quarterlyEmployeeNumbers;
    }

    //----------------------------------------------------

    public CompanyInfo() {
        this.lifeCycle = new LifeCycle();
        this.locationAddress = new CvrAddress();
        this.postalAddress = new CvrAddress();
        this.telephoneNumber = new ValidFromField();
        this.telefaxNumber = new ValidFromField();
        this.email = new ValidFromField();
        this.secondaryIndustries = new ArrayList<IndustryEntity>();
        this.yearlyEmployeeNumbers = new YearlyEmployeeNumbers();
        this.quarterlyEmployeeNumbers = new QuarterlyEmployeeNumbers();
    }

    public void addToJSONObject(JSONObject obj) {
        obj.put("advertProtection", this.hasAdvertProtection());
        obj.put("name",this.getName());
        obj.put("email",this.getEmailText());
        obj.put("phone",this.getTelefaxNumber());
        obj.put("fax",this.getTelefaxNumber());

        obj.put("startDate", this.getLifeCycle().getStartDate());
        obj.put("endDate", this.getLifeCycle().getEndDate());

        obj.put("locationAddress", this.getLocationAddress().toJSON());
        obj.put("postalAddress", this.getPostalAddress().toJSON());
        obj.put("primaryIndustry", this.getPrimaryIndustry().toJSON());

        Collection<IndustryEntity> secondaryIndustries = this.getSecondaryIndustries();
        if (secondaryIndustries != null && !secondaryIndustries.isEmpty()) {
            JSONArray secondaryIndustryArray = new JSONArray();
            for (IndustryEntity secondaryIndustry : secondaryIndustries) {
                secondaryIndustryArray.put(secondaryIndustry.toJSON());
            }
            obj.put("secondaryIndustries", secondaryIndustryArray);
        }

        obj.put("yearlyEmployees", this.getYearlyEmployeeNumbers().toJSON());
        obj.put("quarterlyEmployees", this.getYearlyEmployeeNumbers().toJSON());
    }

}
