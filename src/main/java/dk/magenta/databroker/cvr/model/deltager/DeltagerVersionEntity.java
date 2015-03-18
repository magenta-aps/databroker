package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleEntity;
import dk.magenta.databroker.cvr.model.deltager.status.StatusEntity;
import dk.magenta.databroker.cvr.model.deltager.type.TypeEntity;
import dk.magenta.databroker.cvr.model.embeddable.CvrAddress;
import dk.magenta.databroker.util.Util;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "cvr_deltager_version")
public class DeltagerVersionEntity extends DobbeltHistorikVersion<DeltagerEntity, DeltagerVersionEntity> {

    public DeltagerVersionEntity() {
    }

    public DeltagerVersionEntity(DeltagerEntity entity) {
        super(entity);
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private DeltagerEntity entity;

    @Override
    public DeltagerEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(DeltagerEntity entity) {
        this.entity = entity;
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    //------------------------------------------------------------------------------------------------------------------

    @Column
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
    }




    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    private CompanyVersionEntity companyVersion;

    public CompanyVersionEntity getCompanyVersion() {
        return companyVersion;
    }

    public void setCompanyVersion(CompanyVersionEntity companyVersion) {
        this.companyVersion = companyVersion;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    private CompanyUnitVersionEntity companyUnitVersion;

    public CompanyUnitVersionEntity getCompanyUnitVersion() {
        return companyUnitVersion;
    }

    public void setCompanyUnitVersion(CompanyUnitVersionEntity companyUnit) {
        this.companyUnitVersion = companyUnit;
    }

    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

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
    private Date ajourDate;

    public Date getAjourDate() {
        return ajourDate;
    }

    public void setAjourDate(Date ajourDate) {
        this.ajourDate = ajourDate;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private Date gyldigDate;

    public Date getGyldigDate() {
        return gyldigDate;
    }

    public void setGyldigDate(Date gyldigDate) {
        this.gyldigDate = gyldigDate;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private TypeEntity type;

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private RolleEntity rolle;

    public RolleEntity getRolle() {
        return rolle;
    }

    public void setRolle(RolleEntity rolle) {
        this.rolle = rolle;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private StatusEntity status;

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    //----------------------------------------------------


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


    //----------------------------------------------------

    public boolean matches(String name, String cvrNummer, Date ajourDate, Date gyldigDate, TypeEntity type, RolleEntity rolle, StatusEntity status, CvrAddress locationAddress) {
        return Util.compare(this.name, name) &&
                Util.compare(this.cvrNummer, cvrNummer) &&
                Util.compare(this.ajourDate, ajourDate) &&
                Util.compare(this.gyldigDate, gyldigDate) &&
                Util.compare(this.type, type) &&
                Util.compare(this.rolle, rolle) &&
                Util.compare(this.status, status) &&
                Util.compare(this.locationAddress, locationAddress);
    }

}
