package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public abstract class DataRecord extends Record {
    private String kommuneKode;
    private String vejKode;
    private String timestamp;

    protected int getTimestampStart() {
        return 21;
    }

    public DataRecord(String line) throws ParseException {
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