package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;

import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.service.rest.SearchService;
import dk.magenta.databroker.util.cache.Cacheable;
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
public class LokalitetEntity extends UniqueBase implements OutputFormattable, Cacheable {

    public LokalitetEntity() {
        this.vejstykkeVersioner = new HashSet<VejstykkeVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------

    @ManyToOne
    private KommuneEntity kommune;

    public KommuneEntity getKommune() {
        return kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }

    //----------------------------------------------------

    @Column(nullable = false)
    @Index(name = "navnIndex")
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "lokalitet";
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("navn", this.getNavn());
        obj.put("id", this.getUuid());
        obj.put("href", SearchService.getLokalitetBaseUrl() + "/" + this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray veje = new JSONArray();
        HashSet<KommuneEntity> kommuneEntities = new HashSet<KommuneEntity>();
        for (VejstykkeVersionEntity vejstykkeVersionEntity : this.getVejstykkeVersioner()) {
            if (vejstykkeVersionEntity.getEntity().getLatestVersion() == vejstykkeVersionEntity) {
                VejstykkeEntity entity = vejstykkeVersionEntity.getEntity();
                veje.put(entity.toJSON());
                kommuneEntities.add(entity.getKommune());
            }
        }
        obj.put("veje", veje);
        obj.put("kommune", this.kommune.toJSON());
        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "lokalitet";

    public static Condition lokalitetCondition(SearchParameters parameters) {
        if (parameters.has(Key.LOKALITET)) {
            return RepositoryUtil.whereField(parameters.get(Key.LOKALITET), null, databaseKey+".navn");
        }
        return null;
    }

    public static Condition lokalitetCondition(String lokalitetsNavn) {
        return RepositoryUtil.whereField(lokalitetsNavn, null, databaseKey+".navn");

    }

    public static String joinVej() {
        return databaseKey+".vejstykkeVersioner.entity as "+ VejstykkeEntity.databaseKey;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { ""+this.getKommune().getKode(), this.getNavn() };
    }
}
