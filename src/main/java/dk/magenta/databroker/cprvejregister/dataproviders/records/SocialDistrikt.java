package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class SocialDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_SOCIALDIST;
    }

    private String socialKode;

    public SocialDistrikt(String line) throws ParseException {
        super(line);
        this.socialKode = substr(line, 33, 2);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", socialKode: " + socialKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("socialKode", this.socialKode);
        return obj;
    }
}
