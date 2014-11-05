package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class SogneDistrikt extends Distrikt {

    public String getRecordType() {
        return RECORDTYPE_SOGNEDIST;
    }
    protected int getDistriktsTekstStart() {
        return 37;
    }
    protected int getDistriktsTekstLength() {
        return 20;
    }

    private String myndighedsKode;
    private String myndighedsNavn;

    public SogneDistrikt(String line) throws ParseException {
        super(line);
        this.myndighedsKode = substr(line, 33, 4);
        this.myndighedsNavn = this.distriktsTekst;
        /*
            System.out.println("    Evakueringskode { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                    ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
                    ", ligeUlige: " + ligeUlige + ", myndighedsKode: " + myndighedsKode +
                    ", myndighedsNavn: " + myndighedsNavn + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("myndighedsKode", this.myndighedsKode);
        obj.put("myndighedsNavn", this.myndighedsNavn);
        return obj;
    }
}
