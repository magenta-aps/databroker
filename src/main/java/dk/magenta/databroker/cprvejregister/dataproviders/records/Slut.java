package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Slut extends Record {

    public String getRecordType() {
        return RECORDTYPE_SLUT;
    }

    public Slut(String line) throws ParseException {
        super(line);
        this.put("taeller", substr(line, 4, 8));
    }
}
