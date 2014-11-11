package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class EvakueringsDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_EVAKUERDIST;
    }
    protected int getDistriktsTekstStart() {
        return 34;
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