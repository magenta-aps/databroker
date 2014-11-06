package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class Myndighed extends MyndighedsDataRecord {

    public String getRecordType() {
        return RECORDTYPE_MYNDIGHED;
    }


    protected int getTimestampStart() {
        return 11;
    }


    private String myndighedsGruppe;
    private String telefon;
    private String startDato;
    private String slutDato;
    private String myndighedsNavn;
    private String myndighedsAdressat;
    private String adresselinie1;
    private String adresselinie2;
    private String adresselinie3;
    private String adresselinie4;
    private String telefax;
    private String myndighedsNavnFull;
    private String email;
    private String landekodeA2;
    private String landekodeA3;
    private String landekodeN;

    public Myndighed(String line) throws ParseException {
        super(line);

        this.myndighedsGruppe = substr(line, 10, 1);
        this.telefon = substr(line, 23, 8);
        this.startDato = substr(line, 31, 12);
        this.slutDato = substr(line, 43, 12);
        this.myndighedsNavn = substr(line, 55, 20);
        this.myndighedsAdressat = substr(line, 75, 34);
        this.adresselinie1 = substr(line, 109, 34);
        this.adresselinie2 = substr(line, 143, 34);
        this.adresselinie3 = substr(line, 177, 34);
        this.adresselinie4 = substr(line, 211, 34);
        this.telefax = substr(line, 245, 8);
        this.myndighedsNavnFull = substr(line, 253, 60);
        this.email = substr(line, 313, 100);
        this.landekodeA2 = substr(line, 413, 2);
        this.landekodeA3 = substr(line, 415, 3);
        this.landekodeN = substr(line, 418, 3);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("myndighedsGruppe", this.myndighedsGruppe);
        obj.put("telefon", this.telefon);
        obj.put("startDato", this.startDato);
        obj.put("slutDato", this.slutDato);
        obj.put("myndighedsNavn", this.myndighedsNavn);
        obj.put("myndighedsAdressat", this.myndighedsAdressat);
        obj.put("adresselinie1", this.adresselinie1);
        obj.put("adresselinie2", this.adresselinie2);
        obj.put("adresselinie3", this.adresselinie3);
        obj.put("adresselinie4", this.adresselinie4);
        obj.put("telefax", this.telefax);
        obj.put("myndighedsNavnFull", this.myndighedsNavnFull);
        obj.put("email", this.email);
        obj.put("landekodeA2", this.landekodeA2);
        obj.put("landekodeA3", this.landekodeA3);
        obj.put("landekodeN", this.landekodeN);
        return obj;
    }
}