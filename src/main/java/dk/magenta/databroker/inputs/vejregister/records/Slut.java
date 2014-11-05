package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Slut extends Record {

    public String getRecordType() {
        return RECORDTYPE_SLUT;
    }

    private String taeller;
    public Slut(String line) throws ParseException {
        super(line);
        this.taeller = substr(line, 4, 8);
        /*
        System.out.println("    Slut { tæller: " + taeller + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("taeller", this.taeller);
        return obj;
    }
}
