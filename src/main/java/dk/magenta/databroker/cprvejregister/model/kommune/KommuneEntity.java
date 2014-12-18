package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.reserveretvejnavn.ReserveretVejnavnEntity;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.xml.soap.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommune")
public class KommuneEntity
        extends DobbeltHistorikBase<KommuneEntity, KommuneVersionEntity>
        implements Serializable, OutputFormattable {


    protected KommuneEntity() {
        this.versioner = new ArrayList<KommuneVersionEntity>();
    }

    public static KommuneEntity create() {
        KommuneEntity entity = new KommuneEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<KommuneVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER)
    private KommuneVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private KommuneVersionEntity preferredVersion;


    @Override
    public Collection<KommuneVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public KommuneVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(KommuneVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public KommuneVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(KommuneVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

    @Override
    protected KommuneVersionEntity createVersionEntity() {
        return new KommuneVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Index(name="kommuneKode")
    @Column(name = "kommune_kode", nullable = false, insertable = true, updatable = true, unique = true)
    private int kommunekode;

    @OneToMany(mappedBy = "kommune", fetch = FetchType.LAZY)
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

    @OneToMany(mappedBy = "ansvarligKommune", fetch = FetchType.LAZY)
    private Collection<NavngivenVejVersionEntity> ansvarligForNavngivneVeje;

    @OneToMany(mappedBy = "reserveretAfKommune", fetch = FetchType.LAZY)
    private Collection<ReserveretVejnavnEntity> reserveredeVejnavne;

    public int getKommunekode() {
        return this.kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.kommunekode = kommunekode;
    }

    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return this.kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
    }

    public Collection<NavngivenVejVersionEntity> getAnsvarligForNavngivneVeje() {
        return ansvarligForNavngivneVeje;
    }

    public void setAnsvarligForNavngivneVeje(Collection<NavngivenVejVersionEntity> ansvarligForNavngivneVeje) {
        this.ansvarligForNavngivneVeje = ansvarligForNavngivneVeje;
    }

    public Collection<ReserveretVejnavnEntity> getReserveredeVejnavne() {
        return this.reserveredeVejnavne;
    }

    public void setReserveredeVejnavne(Collection<ReserveretVejnavnEntity> reserveredeVejnavne) {
        this.reserveredeVejnavne = reserveredeVejnavne;
    }

    //------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("navn", this.getLatestVersion().getNavn());
        obj.put("kommuneKode", this.getKommunekode());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray delveje = new JSONArray();
        for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : this.getKommunedeleAfNavngivenVej()) {
            JSONObject delvej = kommunedelAfNavngivenVejEntity.toJSON();
            delvej.put("vej", kommunedelAfNavngivenVejEntity.getNavngivenVejVersion().getEntity().toJSON());
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
            SOAPElement node = parent.addChildElement("kommune");
            node.addAttribute(envelope.createName("navn"), this.getLatestVersion().getNavn());
            node.addAttribute(envelope.createName("kommuneKode"), ""+this.getKommunekode());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        try {
            SOAPElement delveje = node.addChildElement(envelope.createName("delveje"));
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : this.getKommunedeleAfNavngivenVej()) {
                SOAPElement delvej = kommunedelAfNavngivenVejEntity.toXML(delveje, envelope);
                kommunedelAfNavngivenVejEntity.getKommune().toXML(delvej, envelope);
                if (kommunedelAfNavngivenVejEntity.getLokalitet() != null) {
                    kommunedelAfNavngivenVejEntity.getLokalitet().toXML(delvej, envelope);
                }
                kommunedelAfNavngivenVejEntity.getNavngivenVejVersion().getEntity().toXML(delvej, envelope);
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return node;
    }

}
