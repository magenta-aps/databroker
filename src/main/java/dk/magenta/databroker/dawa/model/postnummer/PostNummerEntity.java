package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;
import dk.magenta.databroker.service.rest.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_postnummer")
public class PostNummerEntity extends DobbeltHistorikBase<PostNummerEntity, PostNummerVersionEntity> implements OutputFormattable {

    @OneToMany(mappedBy="entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<PostNummerVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PostNummerVersionEntity latestVersion;

    @OneToOne
    private PostNummerVersionEntity preferredVersion;

    public PostNummerEntity() {
        this.versioner = new ArrayList<PostNummerVersionEntity>();
        this.vejstykkeVersioner = new HashSet<VejstykkeVersionEntity>();
        this.generateNewUUID();
    }

    @Override
    public Collection<PostNummerVersionEntity> getVersioner() {
        return versioner;
    }

    protected void setVersioner(Collection<PostNummerVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public PostNummerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(PostNummerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public PostNummerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(PostNummerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected PostNummerVersionEntity createVersionEntity() {
        return new PostNummerVersionEntity(this);
    }

    /* Domain specific fields */
    @OneToMany(mappedBy = "postnummer")
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

    public String getTypeName() {
        return "postnummer";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        PostNummerVersionEntity version = this.latestVersion;
        obj.put("navn", version.getNavn());
        obj.put("nummer", version.getNr());
        obj.put("href", SearchService.getPostnummerBaseUrl() + "/" + version.getNr());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray kommuner = new JSONArray();
        for (KommuneEntity kommuneEntity : this.getLatestVersion().getKommuner()) {
            kommuner.put(kommuneEntity.toJSON());
        }
        obj.put("kommuner", kommuner);
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
