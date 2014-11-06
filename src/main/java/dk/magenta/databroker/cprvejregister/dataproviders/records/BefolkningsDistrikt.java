package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class BefolkningsDistrikt extends Distrikt {


    public String getRecordType() {
        return RECORDTYPE_BEFOLKDIST;
    }
    protected int getDistriktsTekstStart() {
        return 37;
    }

    private String befolkningsKode;

    public BefolkningsDistrikt(String line) throws ParseException {
        super(line);
        this.befolkningsKode = substr(line, 33, 4);
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", befolkningsKode: " + befolkningsKode +
                    ", distriktsTekst: " + distriktsTekst + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("befolkningsKode", this.befolkningsKode);
        return obj;
    }
}
