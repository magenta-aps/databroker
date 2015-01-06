package dk.magenta.databroker.dawa.model.supplerendebynavne;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_supplerende_bynavn")
public class SupplerendeByNavnEntity extends UniqueBase {

    @Column
    private String supplerendeByNavn;

    public String getSupplerendeByNavn() {
        return supplerendeByNavn;
    }

    public void setSupplerendeByNavn(String supplerendeByNavn) {
        this.supplerendeByNavn = supplerendeByNavn;
    }

    //----------------------------------------------------

    @Column
    private int kommunekode;

    public int getKommunekode() {
        return kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.kommunekode = kommunekode;
    }

    //----------------------------------------------------

    @OneToOne
    private PostNummerEntity postnummer;

    public PostNummerEntity getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(PostNummerEntity postnummer) {
        this.postnummer = postnummer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "supplerendeBynavn";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("supplerendeBynavn", this.supplerendeByNavn);
        obj.put("kommunekode", this.kommunekode);
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        if (this.postnummer != null) {
            obj.put("postnr", this.postnummer.toJSON());
        }
        return obj;
    }
}
