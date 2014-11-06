package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class AktivVej extends VejDataRecord {

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

    public AktivVej(String line) throws ParseException {
        super(line);
        this.tilKommuneKode = substr(line, 24, 4);
        this.tilVejKode = substr(line, 28, 4);
        this.fraKommuneKode = substr(line, 32, 4);
        this.fraVejKode = substr(line, 36, 4);
        this.startDato = substr(line, 40, 12);
        this.vejAdresseringsnavn = substr(line, 52, 20);
        this.vejNavn = substr(line, 72, 40);
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

    /*
        To save in database:
        Locate KommunedelAfNavngivenVej instance by vejkode
            if one doesn't exist, create one
        find NavngivenVej instance using the KommunedelAfNavngivenVej instance
            if one doesn't exist, create one
        in NavngivenVej instance, set vejnavn, vejadresseringsnavn
        find Kommune instance by kommuneKode
        add relation to found Kommune instance
    */
}
