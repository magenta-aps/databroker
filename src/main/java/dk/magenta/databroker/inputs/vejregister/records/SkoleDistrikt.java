package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

/**
 * Created by lars on 04-11-14.
 */
public class SkoleDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 35;
    protected static int distriktsTekstLength = 30;
    public String getRecordType() {
        return RECORDTYPE_SKOLEDIST;
    }

    private String skoleKode;

    public SkoleDistrikt(String line) throws Exception {
        super(line);
        this.skoleKode = substr(line, 33, 2);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", skoleKode: " + skoleKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("skoleKode", this.skoleKode);
        return obj;
    }

}
