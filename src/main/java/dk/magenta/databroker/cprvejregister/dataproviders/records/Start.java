package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Start extends Record {

    public String getRecordType() {
        return RECORDTYPE_START;
    }

    private String opgaveNr;
    private String prodDato;
    public Start(String line) throws ParseException {
        super(line);
        this.opgaveNr = substr(line,4,6);
        this.prodDato = substr(line,10,8);
        /*
        System.out.println("    Start { opgaveNr: "+opgaveNr+", prodDato: "+prodDato+" }");
        */
    }


    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("opgaveNr", this.opgaveNr);
        obj.put("prodDato", this.prodDato);
        return obj;
    }
}
