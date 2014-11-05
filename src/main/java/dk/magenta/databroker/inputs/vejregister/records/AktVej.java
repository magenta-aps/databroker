package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class AktVej extends DataRecord {

    public String getRecordType() {
        return RECORDTYPE_AKTVEJ;
    }

    protected int getTimestampStart() {
        return 12;
    }

    private String tilKommuneKode;
    private String tilVejKode;
    private String fraKommuneKode;
    private String fraVejKode;
    private String startDato;
    private String vejAdresseringsnavn;
    private String vejNavn;

    public AktVej(String line) throws ParseException {
        super(line);
        this.tilKommuneKode = substr(line, 24, 4);
        this.tilVejKode = substr(line, 28, 4);
        this.fraKommuneKode = substr(line, 32, 4);
        this.fraVejKode = substr(line, 36, 4);
        this.startDato = substr(line, 40, 12);
        this.vejAdresseringsnavn = substr(line, 52, 20);
        this.vejNavn = substr(line, 72, 40);

        /*
        System.out.println("    Aktvej { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
                ", timestamp: " + timestamp + ", tilKommuneKode: " + tilKommuneKode +
                ", tilVejKode: " + tilVejKode + ", fraKommuneKode: " + fraKommuneKode +
                ", fraVejKode: " + fraVejKode + ", startDato: " + startDato +
                ", vejAdresseringsnavn: " + vejAdresseringsnavn + ", vejNavn: " + vejNavn + " }");
                */
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("tilKommuneKode", this.tilKommuneKode);
        obj.put("tilVejKode", this.tilVejKode);
        obj.put("fraKommuneKode", this.fraKommuneKode);
        obj.put("fraVejKode", this.fraVejKode);
        obj.put("startDato", this.startDato);
        obj.put("vejAdresseringsnavn", this.vejAdresseringsnavn);
        obj.put("vejNavn", this.vejNavn);
        return obj;
    }
}
