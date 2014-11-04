package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

/**
 * Created by lars on 04-11-14.
 */
public class ValgDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 35;
    protected static int distriktsTekstLength = 30;
    public String getRecordType() {
        return RECORDTYPE_VALGDIST;
    }

    private String valgKode;

    public ValgDistrikt(String line) throws Exception {
        super(line);
        this.valgKode = substr(line, 33, 2);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", valgKode: " + valgKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("valgKode", this.valgKode);
        return obj;
    }
}
