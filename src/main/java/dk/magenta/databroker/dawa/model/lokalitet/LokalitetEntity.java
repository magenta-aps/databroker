package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;

import dk.magenta.databroker.service.rest.SearchService;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by lars on 05-01-2015.
 */
@Entity
@Table(name = "dawa_lokalitet")
public class LokalitetEntity extends UniqueBase implements OutputFormattable {


    public LokalitetEntity() {
        this.vejstykkeVersioner = new HashSet<VejstykkeVersionEntity>();
        this.generateNewUUID();
    }

    /* Domain specific fields */
    @ManyToMany(mappedBy = "lokaliteter")
    private Collection<VejstykkeVersionEntity> vejstykkeVersioner;

    public Collection<VejstykkeVersionEntity> getVejstykkeVersioner() {
        return vejstykkeVersioner;
    }

    public void addVejstykkeVersion(VejstykkeVersionEntity vejstykkeVersionEntity) {
        this.vejstykkeVersioner.add(vejstykkeVersionEntity);
    }
    public void removeVejstykkeVersion(VejstykkeVersionEntity vejstykkeVersionEntity) {
        this.vejstykkeVersioner.remove(vejstykkeVersionEntity);
    }

    @ManyToOne
    private KommuneEntity kommune;

    public KommuneEntity getKommune() {
        return kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }

    @Column(nullable = false)
    @Index(name = "navn")
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }





    public String getTypeName() {
        return "lokalitet";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("navn", this.getNavn());
        obj.put("id", this.getUuid());
        obj.put("href", SearchService.getLokalitetBaseUrl() + "/" + this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();

        JSONArray veje = new JSONArray();
        for (VejstykkeVersionEntity vejstykkeVersionEntity : this.getVejstykkeVersioner()) {
            if (vejstykkeVersionEntity.getEntity().getLatestVersion() == vejstykkeVersionEntity) {
                veje.put(vejstykkeVersionEntity.getEntity().toJSON());
            }
        }
        obj.put("veje", veje);
        return obj;
    }

}
