package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public abstract class PostByRecord extends Record {

    private String kommuneKode;
    private String vejKode;
    private String myndighedsNavn;
    private String vejadresseringsNavn;
    private String husNrFra;
    private String husNrTil;
    private String ligeUlige;

    public PostByRecord(String line) throws ParseException {
        super(line);
        this.kommuneKode = substr(line, 4, 4);
        this.vejKode = substr(line, 8, 4);
        this.myndighedsNavn = substr(line, 12, 20);
        this.vejadresseringsNavn = substr(line, 32, 20);
        this.husNrFra = substr(line, 52, 4);
        this.husNrTil = substr(line, 56, 4);
        this.ligeUlige = substr(line, 60, 1);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("kommuneKode", this.kommuneKode);
        obj.put("vejKode", this.vejKode);
        obj.put("myndighedsNavn", this.myndighedsNavn);
        obj.put("vejadresseringsNavn", this.vejadresseringsNavn);
        obj.put("husNrFra", this.husNrFra);
        obj.put("husNrTil", this.husNrTil);
        obj.put("ligeUlige", this.ligeUlige);
        return obj;
    }
}