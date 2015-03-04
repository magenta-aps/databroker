package dk.magenta.databroker.cvr.model.embeddable;

import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class CvrAddress {
    @Column
    // "gyldigFra": "date",
    private Date validFrom;

    @Column
    // "vejnavn": "varchar(40)",
    private String roadName;

    @Column
    // "vejkode": "unsigned int(4)",
    private int roadCode;

    @Column
    // "husnummerFra": "unsigned int(3)",
    private int housenumberFrom;

    @Column
    // "husnummerTil": "unsigned int(3)",
    private int housenumberTo;

    @Column
    // "bogstavFra": "char(1)",
    private Character letterFrom;

    @Column
    // "bogstavtil": "char(1)",
    private Character letterTo;

    @Column
    // "etage": "unsigned int(2)",
    private int floor;

    @Column
    // "sidedoer": "varchar(4)",
    private String sideOrDoor;

    @Column
    //"postnr": "unsigned int(4)",
    private int postalCode;

    @Column
    // "postdistrikt": "varchar(25)",
    private String postalDistrict;

    @Column
    // "bynavn": "varchar(34)",
    private String cityName;

    @Column
    // "kommune": {
    //    "kode": "unsigned int(2)",
    //    "tekst": "varchar(50)"
    // },
    private int municipalityCode;
    @Column
    private String municipalityText;

    @Column
    // "postboks": "unsigned int(4)",
    private int postBox;

    @Column
    // "coNavn": "varchar(40)",
    private String coName;

    @Column
    private String freetextAddress;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date valid_from) {
        this.validFrom = valid_from;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String road_name) {
        this.roadName = road_name;
    }

    public int getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(int rode_code) {
        this.roadCode = rode_code;
    }

    public int getHousenumberFrom() {
        return housenumberFrom;
    }

    public void setHousenumberFrom(int housenumberFrom) {
        this.housenumberFrom = housenumberFrom;
    }

    public int getHousenumberTo() {
        return housenumberTo;
    }

    public void setHousenumberTo(int housenumberTo) {
        this.housenumberTo = housenumberTo;
    }

    public Character getLetterFrom() {
        return letterFrom;
    }

    public void setLetterFrom(Character bogstavFra) {
        this.letterFrom = bogstavFra;
    }

    public Character getLetterTo() {
        return letterTo;
    }

    public void setLetterTo(Character bogstavTil) {
        this.letterTo = bogstavTil;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getSideOrDoor() {
        return sideOrDoor;
    }

    public void setSideOrDoor(String sideOrDoor) {
        this.sideOrDoor = sideOrDoor;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalDistrict() {
        return postalDistrict;
    }

    public void setPostalDistrict(String postalDistrict) {
        this.postalDistrict = postalDistrict;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(int municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getMunicipalityText() {
        return municipalityText;
    }

    public void setMunicipalityText(String municipalityText) {
        this.municipalityText = municipalityText;
    }

    public int getPostBox() {
        return postBox;
    }

    public void setPostBox(int postBox) {
        this.postBox = postBox;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getFreetextAddress() {
        return freetextAddress;
    }

    public void setFreetextAddress(String freetextAddress) {
        this.freetextAddress = freetextAddress;
    }

    @Column(nullable = true)
    private String descriptor;

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @ManyToOne
    // Relation to EnhedsAdresseEntity, if one was found
    private EnhedsAdresseEntity enhedsAdresse;

    public EnhedsAdresseEntity getEnhedsAdresse() {
        return enhedsAdresse;
    }

    public void setEnhedsAdresse(EnhedsAdresseEntity enhedsAdresseEntity) {
        this.enhedsAdresse = enhedsAdresseEntity;
    }

    public CvrAddress() {

    }

    public CvrAddress(
            Date validFrom,
            String roadName,
            int roadCode,
            int housenumberFrom,
            int housenumberTo,
            Character letterFrom,
            Character letterTo,
            int floor,
            String sideOrDoor,
            int postalCode,
            String postalDistrict,
            String cityName,
            int municipalityCode,
            String municipalityText,
            int postBox,
            String coName,
            String freetextAddress,
            EnhedsAdresseEntity enhedsAdresse
    ) {
        this.validFrom = validFrom;
        this.roadName = roadName;
        this.roadCode = roadCode;
        this.housenumberFrom = housenumberFrom;
        this.housenumberTo = housenumberTo;
        this.letterFrom = letterFrom;
        this.letterTo = letterTo;
        this.floor = floor;
        this.sideOrDoor = sideOrDoor;
        this.postalCode = postalCode;
        this.postalDistrict = postalDistrict;
        this.cityName = cityName;
        this.municipalityCode = municipalityCode;
        this.municipalityText = municipalityText;
        this.postBox = postBox;
        this.coName = coName;
        this.freetextAddress = freetextAddress;
        this.enhedsAdresse = enhedsAdresse;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("validFrom", this.validFrom);
        obj.put("roadName", this.roadName);
        obj.put("housenumberFrom", this.housenumberFrom);
        obj.put("housenumberTo", this.housenumberTo);
        obj.put("letterTo", this.letterTo);
        obj.put("floor", this.floor);
        obj.put("sideOrDoor", this.sideOrDoor);
        obj.put("postalCode", this.postalCode);
        obj.put("postalDistrict", this.postalDistrict);
        obj.put("municipalityCode", this.municipalityCode);
        obj.put("municipalityText", this.municipalityText);
        obj.put("postBox", this.postBox);
        obj.put("coName", this.coName);
        obj.put("freetextAddress", this.freetextAddress);
        obj.put("validFrom", this.validFrom);
        if(this.enhedsAdresse == null) {
            obj.put("cprAddress", this.enhedsAdresse);
        } else {
            obj.put("cprAddress", this.enhedsAdresse.toJSON());
        }
        return obj;
    }
}
