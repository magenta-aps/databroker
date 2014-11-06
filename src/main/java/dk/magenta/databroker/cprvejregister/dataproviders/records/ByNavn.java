package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class ByNavn extends PostByRecord {

    public static final String RECORDTYPE_BYNAVN = "001";

    public String getRecordType() {
        return RECORDTYPE_BYNAVN;
    }

    private String byNavn;

    public ByNavn(String line) throws ParseException {
        super(line);
        this.byNavn = substr(line, 61, 34);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("byNavn", this.byNavn);
        return obj;
    }
}
