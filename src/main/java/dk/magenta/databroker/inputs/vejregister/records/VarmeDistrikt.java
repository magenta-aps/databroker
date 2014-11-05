package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class VarmeDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_VARMEDIST;
    }
    protected int getDistriktsTekstStart() {
        return 37;
    }

    private String varmeKode;

    public VarmeDistrikt(String line) throws ParseException {
        super(line);
        this.varmeKode = substr(line, 33, 4);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", valgKode: " + valgKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("varmeKode", this.varmeKode);
        return obj;
    }
}
