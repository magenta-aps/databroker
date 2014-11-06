package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public abstract class VejDataRecord extends Record {

    public static final String RECORDTYPE_AKTVEJ = "001";
    public static final String RECORDTYPE_BOLIG = "002";
    public static final String RECORDTYPE_BYDISTRIKT = "003";
    public static final String RECORDTYPE_POSTDIST = "004";
    public static final String RECORDTYPE_NOTATVEJ = "005";
    public static final String RECORDTYPE_BYFORNYDIST = "006";
    public static final String RECORDTYPE_DIVDIST = "007";
    public static final String RECORDTYPE_EVAKUERDIST = "008";
    public static final String RECORDTYPE_KIRKEDIST = "009";
    public static final String RECORDTYPE_SKOLEDIST = "010";
    public static final String RECORDTYPE_BEFOLKDIST = "011";
    public static final String RECORDTYPE_SOCIALDIST = "012";
    public static final String RECORDTYPE_SOGNEDIST = "013";
    public static final String RECORDTYPE_VALGDIST = "014";
    public static final String RECORDTYPE_VARMEDIST = "015";
    public static final String RECORDTYPE_HISTORISKVEJ = "016";

    private String kommuneKode;
    private String vejKode;
    private String timestamp;

    protected int getTimestampStart() {
        return 21;
    }

    public VejDataRecord(String line) throws ParseException {
        super(line);
        this.kommuneKode = substr(line, 4, 4);
        this.vejKode = substr(line, 8, 4);
        this.timestamp = substr(line, this.getTimestampStart(), 12);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kommuneKode", this.kommuneKode);
        obj.put("vejKode", this.vejKode);
        obj.put("timestamp", this.timestamp);
        return obj;
    }
}