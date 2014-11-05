package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class ByfornyelsesDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 39;
    protected static int distriktsTekstLength = 30;
    public String getRecordType() {
        return RECORDTYPE_BYFORNYDIST;
    }

    String byfornyKode;
    public ByfornyelsesDistrikt(String line) throws ParseException {
        super(line);
        this.byfornyKode = substr(line, 33, 6);
        /*
        System.out.println("    Byfornyelsesdistrikt { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", byfornyKode: " + byfornyKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
                    */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("byfornyKode", this.byfornyKode);
        return obj;
    }
}
