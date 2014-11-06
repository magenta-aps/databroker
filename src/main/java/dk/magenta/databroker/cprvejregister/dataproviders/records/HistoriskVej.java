package dk.magenta.databroker.cprvejregister.dataproviders.records;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lars on 05-11-14.
 */
public class HistoriskVej extends DataRecord {

    public String getRecordType() {
        return RECORDTYPE_HISTORISKVEJ;
    }
    protected int getTimestampStart() {
        return 12;
    }

    private String startDato;
    private String slutDato;
    private String vejAdresseringsnavn;
    private String vejNavn;

    public HistoriskVej(String line) throws ParseException {
        super(line);
        this.startDato = substr(line, 24, 12);
        this.slutDato = substr(line, 36, 12);
        this.vejAdresseringsnavn = substr(line, 48, 20);
        this.vejNavn = substr(line, 68, 40);
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("startDato", this.startDato);
        obj.put("slutDato", this.slutDato);
        obj.put("vejAdresseringsnavn", this.vejAdresseringsnavn);
        obj.put("vejNavn", this.vejNavn);
        return obj;
    }
}
