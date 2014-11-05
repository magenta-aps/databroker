package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Bolig extends DataRecord {

    public String getRecordType() {
        return RECORDTYPE_BOLIG;
    }
    protected int getTimestampStart() {
        return 22;
    }

    protected String husNr;
    protected String etage;
    protected String sidedoer;
    protected String startDato;
    protected String lokalitet;

    public Bolig(String line) throws ParseException {
        super(line);

        this.husNr = substr(line, 12, 4);
        this.etage = substr(line, 16, 2);
        this.sidedoer = substr(line, 18, 4);
        this.startDato = substr(line, 35, 12);
        this.lokalitet = substr(line, 59, 34);
        /*
        System.out.println("    Bolig { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                ", timestamp: " + timestamp + ", husNr: " + husNr + ", etage: " + etage +
                ", sidedoer: " + sidedoer + ", startDato: " + startDato + ", lokalitet: " + lokalitet + " }");
                */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("husNr", this.husNr);
        obj.put("etage", this.etage);
        obj.put("sidedoer", this.sidedoer);
        obj.put("startDato", this.startDato);
        obj.put("lokalitet", this.lokalitet);
        return obj;
    }
}
