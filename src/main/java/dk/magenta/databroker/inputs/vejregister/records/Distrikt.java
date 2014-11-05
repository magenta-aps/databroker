package dk.magenta.databroker.inputs.vejregister.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */

public abstract class Distrikt extends DataRecord {

    protected static int distriktsTekstStart = 35;
    protected static int distriktsTekstLength = 30;


    protected String husNrFra;
    protected String husNrTil;
    protected String ligeUlige;
    protected String distriktsTekst;

    public Distrikt(String line) throws ParseException {
        super(line);
        this.husNrFra = substr(line, 12, 4);
        this.husNrTil = substr(line, 16, 4);
        this.ligeUlige = substr(line, 20, 1);
        this.distriktsTekst = substr(line, distriktsTekstStart, distriktsTekstLength);

        //System.out.println("    Befolkningsdistrikt { kommuneKode: " + kommuneKode + ", vejKode: " + vejKode +
         //       ", timestamp: " + timestamp + ", husNrFra: " + husNrFra + ", husNrTil: " + husNrTil +
        //        ", ligeUlige: " + ligeUlige + ", befolkKode: " + befolkKode +
         //       ", distriktsTekst: " + distriktsTekst + " }");
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("husNrFra", this.husNrFra);
        obj.put("husNrTil", this.husNrTil);
        obj.put("ligeUlige", this.ligeUlige);
        obj.put("distriktsTekst", this.distriktsTekst);
        return obj;
    }

}