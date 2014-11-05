package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class NotatVej extends DataRecord {

    public String getRecordType() {
        return RECORDTYPE_NOTATVEJ;
    }
    protected static int timestampStart = 54;

    protected String notatNr;
    protected String notatLinie;
    protected String startDato;

    public NotatVej(String line) throws ParseException {
        super(line);
        this.notatNr = substr(line, 12, 2);
        this.notatLinie = substr(line, 14, 40);
        this.startDato = substr(line, 66, 12);
        /*
        System.out.println("    Notatvej { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                ", timestamp: " + timestamp + ", notatNr: " + notatNr + ", notatLinie: " + notatLinie +
                ", startDato: " + startDato + " }");
        */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("notatNr", this.notatNr);
        obj.put("notatLinie", this.notatLinie);
        obj.put("startDato", this.startDato);
        return obj;
    }

}