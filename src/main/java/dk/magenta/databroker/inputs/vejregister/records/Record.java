package dk.magenta.databroker.inputs.vejregister.records;
import org.json.JSONObject;

/**
 * Created by lars on 04-11-14.
 */
public abstract class Record {

    public static final String RECORDTYPE_START = "000";
    public static final String RECORDTYPE_AKTVEJ = "001";
    public static final String RECORDTYPE_BOLIG = "002";
    public static final String RECORDTYPE_BYNAVN = "003";
    public static final String RECORDTYPE_POSTDIST = "004";
    public static final String RECORDTYPE_NOTATVEJ = "005";
    public static final String RECORDTYPE_BYFORNYDIST = "006";
    public static final String RECORDTYPE_DIVDIST = "009";
    public static final String RECORDTYPE_EVAKUERDIST = "008";
    public static final String RECORDTYPE_KIRKEDIST = "009";
    public static final String RECORDTYPE_SKOLEDIST = "010";
    public static final String RECORDTYPE_BEFOLKDIST = "011";
    public static final String RECORDTYPE_SOCIALDIST = "012";
    public static final String RECORDTYPE_SOGNEDIST = "013";
    public static final String RECORDTYPE_VALGDIST = "014";
    public static final String RECORDTYPE_VARMEDIST = "015";
    public static final String RECORDTYPE_HISTORISKVEJ = "016";
    public static final String RECORDTYPE_SLUT = "999";

    public String getRecordType() {
        return null;
    }

    public Record(String line) throws Exception {
        String type = substr(line, 1, 3);
        if (!type.equals(this.getRecordType())) {
            throw new Exception("Invalid recordtype "+type+" for class "+this.getClass().getName());
        }
    }

    protected String substr(String line, int pos, int length) {
        return line.substring(pos-1, Math.min(pos + length - 1, line.length())).trim();
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        String[] classParts = this.getClass().getCanonicalName().split("\\.");
        obj.put("type", classParts[classParts.length-1]);
        return obj;
    }
}