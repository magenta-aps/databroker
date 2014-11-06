package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class PostNummer extends PostByRecord {
    public static final String RECORDTYPE_POSTNUMMER = "001";

    public String getRecordType() {
        return RECORDTYPE_POSTNUMMER;
    }

    private String postNr;
    private String postDistriktTekst;

    public PostNummer(String line) throws ParseException {
        super(line);
        this.postNr = substr(line, 61, 4);
        this.postDistriktTekst = substr(line, 65, 20);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("postNr", this.postNr);
        obj.put("postDistriktTekst", this.postDistriktTekst);
        return obj;
    }
}