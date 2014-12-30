package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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

    public void setVejstykkeVersioner(Collection<VejstykkeVersionEntity> vejstykkeVersioner) {
        this.vejstykkeVersioner = vejstykkeVersioner;
    }

    public String getTypeName() {
        return "postnummer";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("navn", this.getLatestVersion().getNavn());
        obj.put("nummer", this.getLatestVersion().getNr());
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
