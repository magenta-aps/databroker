package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class PostDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 37;
    protected static int distriktsTekstLength = 20;
    public String getRecordType() {
        return RECORDTYPE_POSTDIST;
    }

    String postNr;
    public PostDistrikt(String line) throws ParseException {
        super(line);
        this.postNr = substr(line, 33, 4);

            //System.out.println("    Postdistrikt { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
            //        ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
            //        ", ligeUlige: " + ligeUlige + ", postNr: " + postNr + ", distriktsTekst: " + distriktsTekst + " }");
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("postNr", this.postNr);
        return obj;
    }
}
