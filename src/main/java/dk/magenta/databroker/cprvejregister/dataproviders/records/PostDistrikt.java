package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class PostDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_POSTDIST;
    }
    protected int getDistriktsTekstStart() {
        return 37;
    }
    protected int getDistriktsTekstLength() {
        return 20;
    }

    private String postNr;
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