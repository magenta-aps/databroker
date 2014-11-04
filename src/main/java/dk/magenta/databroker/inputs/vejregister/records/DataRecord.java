package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

/**
 * Created by lars on 04-11-14.
 */
public abstract class DataRecord extends Record {
    private String kommuneKode;
    private String vejKode;
    private String timestamp;

    protected static int timestampStart = 21;
    protected static String recordType = null;

    public DataRecord(String line) throws Exception {
        super(line);
        this.kommuneKode = substr(line, 4, 4);
        this.vejKode = substr(line, 8, 4);
        this.timestamp = substr(line, timestampStart, 12);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kommuneKode", this.kommuneKode);
        obj.put("vejKode", this.vejKode);
        obj.put("timestamp", this.timestamp);
        return obj;
    }
}