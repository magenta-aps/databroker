package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;
import dk.magenta.databroker.cprvejregister.model.vejnavneomraade.VejnavneomraadeEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "navngiven_vej" )
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class NavngivenVejEntity
        extends DobbeltHistorikBase<NavngivenVejEntity, NavngivenVejVersionEntity>
        implements Serializable, OutputFormattable {


    protected NavngivenVejEntity(){
        this.versioner = new ArrayList<NavngivenVejVersionEntity>();
    }

    public static NavngivenVejEntity create() {
        NavngivenVejEntity entity = new NavngivenVejEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<NavngivenVejVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private NavngivenVejVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private NavngivenVejVersionEntity preferredVersion;

    @Override
    public Collection<NavngivenVejVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public NavngivenVejVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(NavngivenVejVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public NavngivenVejVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(NavngivenVejVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }



    public Collection<NavngivenVejVersionEntity> cleanLatestVersion(KommunedelAfNavngivenVejRepository repository) {
        ArrayList<NavngivenVejVersionEntity> toRemove = new ArrayList<NavngivenVejVersionEntity>();
        if (this.versioner.size() > 1) {
            ArrayList<NavngivenVejVersionEntity> versions = new ArrayList<NavngivenVejVersionEntity>();
            versions.addAll(this.versioner);
            boolean lastOnly = false;
            int dest = (lastOnly ? versions.size() - 2 : 0);
            for (int i = versions.size() - 1; i > dest; i--) { // We must loop from newest to oldest version
                NavngivenVejVersionEntity oldVersion = versions.get(i-1);
                NavngivenVejVersionEntity newVersion = versions.get(i);
                if (oldVersion.compare(newVersion)) {
                    // The two versions are identical; remove the latest of those
                    this.versioner.remove(newVersion);

                    // Since kommunedeleAfNavngivenVej are new entries, replace the old ones
                    Collection<KommunedelAfNavngivenVejEntity> delveje = newVersion.getKommunedeleAfNavngivenVej();
                    for (KommunedelAfNavngivenVejEntity delvej : delveje) {
                        delvej.setNavngivenVejVersion(oldVersion);
                        oldVersion.addKommunedelAfNavngivenVej(delvej);
                    }
                    delveje.clear();

                    if (this.latestVersion == newVersion) {
                        this.latestVersion = oldVersion;
                    }
                    toRemove.add(newVersion);
                }
            }
        }
        return toRemove;
    }


    /*
    * Create the relevant version entity
    * */

     @Override
    protected NavngivenVejVersionEntity createVersionEntity() {
        return new NavngivenVejVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @OneToMany(mappedBy = "navngivenVej", fetch = FetchType.LAZY)
    private Collection<HusnummerEntity> husnumre;

    @OneToOne(fetch = FetchType.LAZY)
    private VejnavneomraadeEntity vejnavneomraade;

    @OneToMany(mappedBy = "navngivenVej", fetch = FetchType.LAZY)
    private Collection<VejnavneforslagEntity> vejnavneforslag;

    public Collection<HusnummerEntity> getHusnumre() {
        return husnumre;
    }

    public void setHusnumre(Collection<HusnummerEntity> husnumre) {
        this.husnumre = husnumre;
    }

    public VejnavneomraadeEntity getVejnavneomraade() {
        return this.vejnavneomraade;
    }

    public void setVejnavneomraade(VejnavneomraadeEntity vejnavneomraade) {
        this.vejnavneomraade = vejnavneomraade;
    }

    public Collection<VejnavneforslagEntity> getVejnavneforslag() {
        return this.vejnavneforslag;
    }

    public void setVejnavneforslag(Collection<VejnavneforslagEntity> vejnavneforslag) {
        this.vejnavneforslag = vejnavneforslag;
    }

    //------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("navn", this.getLatestVersion().getVejnavn());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray delveje = new JSONArray();
        for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : this.getLatestVersion().getKommunedeleAfNavngivenVej()) {
            JSONObject delvej = kommunedelAfNavngivenVejEntity.toJSON();
            delvej.put("kommune", kommunedelAfNavngivenVejEntity.getKommune().toJSON());
            if (kommunedelAfNavngivenVejEntity.getLokalitet() != null) {
                delvej.put("lokalitet", kommunedelAfNavngivenVejEntity.getLokalitet().toJSON());
            }
            delveje.put(delvej);
        }
        obj.put("delveje", delveje);
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("vej");
            node.addAttribute(envelope.createName("navn"), this.getLatestVersion().getVejnavn());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = this.toXML(parent, envelope);
            SOAPElement delveje = node.addChildElement("delveje");
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : this.getLatestVersion().getKommunedeleAfNavngivenVej()) {
                SOAPElement delvej = kommunedelAfNavngivenVejEntity.toXML(delveje, envelope);
                kommunedelAfNavngivenVejEntity.getKommune().toXML(delvej, envelope);
                if (kommunedelAfNavngivenVejEntity.getLokalitet() != null) {
                    kommunedelAfNavngivenVejEntity.getLokalitet().toXML(delvej, envelope);
                }
                delveje.addChildElement(delvej);
            }
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

}
