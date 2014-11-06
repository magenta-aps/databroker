package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class KirkeDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_KIRKEDIST;
    }

    private String kirkeKode;

    public KirkeDistrikt(String line) throws ParseException {
        super(line);
        this.kirkeKode = substr(line, 33, 2);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", kirkeKode: " + kirkeKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kirkeKode", this.kirkeKode);
        return obj;
    }
}
