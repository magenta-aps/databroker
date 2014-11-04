package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

/**
 * Created by lars on 04-11-14.
 */
public class DiverseDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 39;
    protected static int distriktsTekstLength = 30;
    public String getRecordType() {
        return RECORDTYPE_DIVDIST;
    }

    private String distriktType;
    private String diverseDistriktsKode;

    public DiverseDistrikt(String line) throws Exception {
        super(line);
        this.distriktType = substr(line, 33, 2);
        this.diverseDistriktsKode = substr(line, 35, 4);
        /*
            System.out.println("    DiverseDistrikt { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", distriktType: " + distriktType +
                    ", diverseDistriktsKode: " + diverseDistriktsKode +  ", distriktsTekst: " + distriktsTekst + " }");
                    */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("distriktType", this.distriktType);
        obj.put("diverseDistriktsKode", this.diverseDistriktsKode);
        return obj;
    }
}
