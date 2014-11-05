package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class EvakueringsDistrikt extends Distrikt {

    protected static int distriktsTekstStart = 34;
    protected static int distriktsTekstLength = 30;
    public String getRecordType() {
        return RECORDTYPE_EVAKUERDIST;
    }

    private String evakueringsKode;

    public EvakueringsDistrikt(String line) throws ParseException {
        super(line);
        this.evakueringsKode = substr(line, 33, 1);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", evakueringsKode: " + evakueringsKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("evakueringsKode", this.evakueringsKode);
        return obj;
    }
}
