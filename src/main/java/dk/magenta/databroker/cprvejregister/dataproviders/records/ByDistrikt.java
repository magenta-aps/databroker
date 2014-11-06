package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class ByDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_BYDISTRIKT;
    }
    protected int getDistriktsTekstStart() {
        return 33;
    }
    protected int getDistriktsTekstLength() {
        return 34;
    }

    private String bynavn;

    public ByDistrikt(String line) throws ParseException {
        super(line);
        this.bynavn = this.distriktsTekst;
        /*
            System.out.println("    ByDistrikt { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", bynavn: " + bynavn + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("bynavn", this.bynavn);
        return obj;
    }
}
