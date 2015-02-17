package dk.magenta.databroker.dawa.model.ejerlav;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;
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
@Table(name = "dawa_ejerlav")
public class EjerLavEntity extends DobbeltHistorikBase<EjerLavEntity, EjerLavVersionEntity> {

    public EjerLavEntity() {
        this.versioner = new ArrayList<EjerLavVersionEntity>();
        this.adgangsAdresseVersioner = new HashSet<AdgangsAdresseVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy="entity")
    private Collection<EjerLavVersionEntity> versioner;

    public Collection<EjerLavVersionEntity> getVersions() {
        return versioner;
    }

    public void setVersioner(Collection<EjerLavVersionEntity> versioner) {
        this.versioner = versioner;
    }

    //----------------------------------------------------

    @OneToOne
    private EjerLavVersionEntity latestVersion;

    public EjerLavVersionEntity getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(EjerLavVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private EjerLavVersionEntity preferredVersion;

    public EjerLavVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    public void setPreferredVersion(EjerLavVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected EjerLavVersionEntity createVersionEntity() {
        return new EjerLavVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @OneToMany(mappedBy = "ejerlav")
    private Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner;

    public Collection<AdgangsAdresseVersionEntity> getAdgangsAdresseVersioner() {
        return adgangsAdresseVersioner;
    }

    public void setAdgangsAdresseVersioner(Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner) {
        this.adgangsAdresseVersioner = adgangsAdresseVersioner;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "ejerlav";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("kode", this.latestVersion.getKode());
        obj.put("navn", this.latestVersion.getNavn());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        if (this.adgangsAdresseVersioner.size() > 0) {
            JSONArray adgangsadresser = new JSONArray();
            for (AdgangsAdresseVersionEntity adgangsAdresseVersionEntity : this.adgangsAdresseVersioner) {
                if (adgangsAdresseVersionEntity.getEntity().getLatestVersion() == adgangsAdresseVersionEntity) {
                    adgangsadresser.put(adgangsAdresseVersionEntity.getEntity().toJSON());
                }
            }
            obj.put("adgangsadresser", adgangsadresser);
        }
        return obj;
    }
}
