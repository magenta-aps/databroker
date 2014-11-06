package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Lokalitet extends Record {
    public static final String RECORDTYPE_LOKALITET = "001";

    public String getRecordType() {
        return RECORDTYPE_LOKALITET;
    }

    private String kommuneKode;
    private String vejKode;
    private String myndighedsNavn;
    private String vejadresseringsNavn;
    private String husNr;
    private String etage;
    private String sidedoer;
    private String lokalitet;

    public Lokalitet(String line) throws ParseException {
        super(line);

        this.kommuneKode = substr(line, 4, 4);
        this.vejKode = substr(line, 8, 4);
        this.myndighedsNavn = substr(line, 12, 20);
        this.vejadresseringsNavn = substr(line, 32, 20);
        this.husNr = substr(line, 52, 4);
        this.etage = substr(line, 56, 2);
        this.sidedoer = substr(line, 58, 4);
        this.lokalitet = substr(line, 62, 34);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kommuneKode", this.kommuneKode);
        obj.put("vejKode", this.vejKode);
        obj.put("myndighedsNavn", this.myndighedsNavn);
        obj.put("vejadresseringsNavn", this.vejadresseringsNavn);
        obj.put("husNr", this.husNr);
        obj.put("etage", this.etage);
        obj.put("sidedoer", this.sidedoer);
        obj.put("lokalitet", this.lokalitet);
        return obj;
    }
}
