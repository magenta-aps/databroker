package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public abstract class MyndighedsDataRecord extends Record {

    public static final String RECORDTYPE_MYNDIGHED = "001";
    public static final String RECORDTYPE_KOMMUNEREL = "002";

    private String myndighedsKode;
    private String myndighedsType;
    private String timestamp;

    protected int getTimestampStart() {
        return 11;
    }

    public MyndighedsDataRecord(String line) throws ParseException {
        super(line);
        this.myndighedsKode = substr(line, 4, 4);
        this.myndighedsType = substr(line, 8, 2);
        this.timestamp = substr(line, this.getTimestampStart(), 12);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("myndighedsKode", this.myndighedsKode);
        obj.put("myndighedsType", this.myndighedsType);
        obj.put("timestamp", this.timestamp);
        return obj;
    }
}