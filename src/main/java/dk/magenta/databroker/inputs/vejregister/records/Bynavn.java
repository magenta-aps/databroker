package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Bynavn extends Distrikt {

    protected static int distriktsTekstStart = 33;
    protected static int distriktsTekstLength = 34;
    public String getRecordType() {
        return RECORDTYPE_BYNAVN;
    }

    private String bynavn;

    public Bynavn(String line) throws ParseException {
        super(line);
        this.bynavn = this.distriktsTekst;
        /*
            System.out.println("    Bynavn { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
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
