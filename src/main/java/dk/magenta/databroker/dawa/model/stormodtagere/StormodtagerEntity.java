package dk.magenta.databroker.dawa.model.stormodtagere;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_stormodtager")
public class StormodtagerEntity extends UniqueBase {

    //------------------------------------------------------------------------------------------------------------------

    @Column
    private int nr;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    //----------------------------------------------------

    @Column
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    //----------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    private AdgangsAdresseEntity adgangsadresse;

    public AdgangsAdresseEntity getAdgangsadresse() {
        return adgangsadresse;
    }

    public void setAdgangsadresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsadresse = adgangsAdresse;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "stormodtager";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("nr", this.nr);
        obj.put("navn", this.navn);
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        if (this.adgangsadresse != null) {
            obj.put("adgangsadresse", this.adgangsadresse.toJSON());
        }
        return obj;
    }
}
