package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.service.rest.SearchService;
import org.hibernate.annotations.Index;
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
@Table(name = "dawa_vejstykke")
public class VejstykkeEntity extends DobbeltHistorikBase<VejstykkeEntity, VejstykkeVersionEntity> {

    public VejstykkeEntity() {
        this.versioner = new ArrayList<VejstykkeVersionEntity>();
        this.adgangsAdresser = new HashSet<AdgangsAdresseEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<VejstykkeVersionEntity> versioner;

    @Override
    public Collection<VejstykkeVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<VejstykkeVersionEntity> versioner) {
        this.versioner = versioner;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private VejstykkeVersionEntity latestVersion;

    @Override
    public VejstykkeVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(VejstykkeVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private VejstykkeVersionEntity preferredVersion;

    @Override
    public VejstykkeVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(VejstykkeVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected VejstykkeVersionEntity createVersionEntity() {
        return new VejstykkeVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column
    @Index(name="kode")
    private int kode;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
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

    @OneToMany(mappedBy = "vejstykke")
    private Collection<AdgangsAdresseEntity> adgangsAdresser;

    public Collection<AdgangsAdresseEntity> getAdgangsAdresser() {
        return adgangsAdresser;
    }

    public void setAdgangsAdresser(Collection<AdgangsAdresseEntity> adgangsAdresse) {
        this.adgangsAdresser = adgangsAdresse;
    }
    public void addAdgangsAdresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsAdresser.add(adgangsAdresse);
    }

    public void removeAdgangsAdresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsAdresser.remove(adgangsAdresse);
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "vejstykke";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("vejnavn", this.getLatestVersion().getVejnavn());
        obj.put("vejaddresseringsnavn", this.getLatestVersion().getVejadresseringsnavn());
        obj.put("vejkode", this.getKode());
        obj.put("id", this.getUuid());
        obj.put("href", SearchService.getVejBaseUrl() + "/" + this.getUuid());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        if (this.getKommune() != null) {
            obj.put("kommune", this.getKommune().toJSON());
        }
        if (!this.getLatestVersion().getPostnumre().isEmpty()) {
            JSONArray postnumre = new JSONArray();
            for (PostNummerEntity postNummerEntity : this.getLatestVersion().getPostnumre()) {
                postnumre.put(postNummerEntity.toJSON());
            }
            obj.put("postnr", postnumre);
        }
        if (!this.getLatestVersion().getLokaliteter().isEmpty()) {
            JSONArray lokaliteter = new JSONArray();
            for (LokalitetEntity lokalitetEntity : this.getLatestVersion().getLokaliteter()) {
                lokaliteter.put(lokalitetEntity.toJSON());
            }
            obj.put("lokaliteter", lokaliteter);
        }
        return obj;
    }
}
