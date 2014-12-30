package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_vejstykke")
public class VejstykkeEntity extends DobbeltHistorikBase<VejstykkeEntity, VejstykkeVersionEntity> {

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<VejstykkeVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private VejstykkeVersionEntity latestVersion;

    @OneToOne
    private VejstykkeVersionEntity preferredVersion;


    public VejstykkeEntity() {
        this.versioner = new ArrayList<VejstykkeVersionEntity>();
        this.generateNewUUID();
    }

    @Override
    public Collection<VejstykkeVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<VejstykkeVersionEntity> versioner) {
        this.versioner = versioner;
    }

    @Override
    public VejstykkeVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(VejstykkeVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public VejstykkeVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(VejstykkeVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected VejstykkeVersionEntity createVersionEntity() {
        return new VejstykkeVersionEntity(this);
    }

    /* Domain specific fields */

    @Column
    private int kode;

    @ManyToOne
    private KommuneEntity kommune;

    @OneToMany(mappedBy = "vejstykke")
    private Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner;


    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public KommuneEntity getKommune() {
        return kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }


    public Collection<AdgangsAdresseVersionEntity> getAdgangsAdresseVersioner() {
        return adgangsAdresseVersioner;
    }

    public void setAdgangsAdresseVersioner(Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersioner) {
        this.adgangsAdresseVersioner = adgangsAdresseVersioner;
    }



    public String getTypeName() {
        return "vejstykke";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("vejnavn", this.getLatestVersion().getVejnavn());
        obj.put("vejaddresseringsnavn", this.getLatestVersion().getVejadresseringsnavn());
        obj.put("vejkode", this.getKode());
        obj.put("id", this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        if (this.getKommune() != null) {
            obj.put("kommune", this.getKommune().toJSON());
        }
        if (this.getLatestVersion().getPostnummer() != null) {
            obj.put("postnr", this.getLatestVersion().getPostnummer().toJSON());
        }
        return obj;
    }
}
