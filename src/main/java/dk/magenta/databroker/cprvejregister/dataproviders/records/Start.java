package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Start extends Record {

    public String getRecordType() {
        return RECORDTYPE_START;
    }

    public Start(String line) throws ParseException {
        super(line);
        this.put("opgaveNr", substr(line,4,6));
        this.put("prodDato", substr(line,10,8));
    }

}
