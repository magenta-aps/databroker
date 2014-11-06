package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class KommuneRelation extends MyndighedsDataRecord {

    public String getRecordType() {
        return RECORDTYPE_KOMMUNEREL;
    }


    protected int getTimestampStart() {
        return 15;
    }


    private String kommuneKode;

    public KommuneRelation(String line) throws ParseException {
        super(line);
        this.kommuneKode = substr(line, 11, 4);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kommuneKode", this.kommuneKode);
        return obj;
    }
}