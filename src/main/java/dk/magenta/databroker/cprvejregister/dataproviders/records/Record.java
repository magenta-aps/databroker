package dk.magenta.databroker.cprvejregister.dataproviders.records;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by lars on 04-11-14.
 */
public abstract class Record extends HashMap<String, String> {

    public static final String RECORDTYPE_START = "000";
    public static final String RECORDTYPE_SLUT = "999";

    private HashMap<String, String> values;

    public String getRecordType() {
        return null;
    }

    public String getRecordClass() {
        String[] classParts = this.getClass().getCanonicalName().split("\\.");
        return classParts[classParts.length-1];
    }

    public Record(String line) throws ParseException {
        if (line == null) {
            throw new ParseException("Invalid NULL input.", 0);
        }
        String type = substr(line, 1, 3);
        String thisType = this.getRecordType();
        if (!type.equals(thisType)) {
            throw new ParseException("Invalid recordtype "+type+" for class "+this.getClass().getName()+", was expecting the input to begin with "+thisType+". Input was "+line+".", 0);
        }
        this.values = new HashMap<String, String>();
    }

    protected String substr(String line, int position, int length) {
        return line.substring(Math.min(position - 1, line.length()), Math.min(position + length - 1, line.length())).trim();
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", this.getRecordClass());
        for (String key : this.keySet()) {
            obj.put(key, this.get(key));
        }
        return obj;
    }
}