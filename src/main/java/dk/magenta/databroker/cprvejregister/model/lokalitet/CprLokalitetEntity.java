package dk.magenta.databroker.cprvejregister.model.lokalitet;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 12/12/14.
 */
@Entity
@Table(name = "lokalitet")
public class CprLokalitetEntity
        extends DobbeltHistorikBase<CprLokalitetEntity, CprLokalitetVersionEntity>
        implements Serializable, OutputFormattable {

    public CprLokalitetEntity() {
        this.versioner = new ArrayList<CprLokalitetVersionEntity>();
    }

    public static CprLokalitetEntity create() {
        CprLokalitetEntity lokalitetEntity = new CprLokalitetEntity();
        lokalitetEntity.generateNewUUID();
        return lokalitetEntity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CprLokalitetVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CprLokalitetVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private CprLokalitetVersionEntity preferredVersion;

    @Override
    public Collection<CprLokalitetVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public CprLokalitetVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CprLokalitetVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public CprLokalitetVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CprLokalitetVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }



    @Override
    protected CprLokalitetVersionEntity createVersionEntity() {
        return new CprLokalitetVersionEntity(this);
    }

    /*
    * Fields on the entity
    * */

    @Basic
    @Index(name = "lokalitetsKode")
    @Column(name = "lokalitetskode", nullable = false, insertable = true, updatable = true)
    private int lokalitetsKode;

    @OneToMany(mappedBy = "lokalitet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;




    public int getLokalitetsKode() {
        return this.lokalitetsKode;
    }

    public void setLokalitetsKode(int lokalitetsKode) {
        this.lokalitetsKode = lokalitetsKode;
    }

    public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
        return kommunedeleAfNavngivenVej;
    }

    public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVej) {
        this.kommunedeleAfNavngivenVej = kommunedelAfNavngivenVej;
    }

    public CprKommuneEntity getKommune() {
        for (KommunedelAfNavngivenVejEntity delvej : this.getKommunedeleAfNavngivenVej()) {
            return delvej.getKommune();
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "lokalitet";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("lokalitetsKode", this.getLokalitetsKode());
        obj.put("lokalitetsNavn", this.getLatestVersion().getLokalitetsNavn());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray delveje = new JSONArray();
        for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : this.getKommunedeleAfNavngivenVej()) {
            JSONObject delvej = kommunedelAfNavngivenVejEntity.toJSON();
            delvej.put("kommune", kommunedelAfNavngivenVejEntity.getKommune().toJSON());
            delvej.put("vej", kommunedelAfNavngivenVejEntity.getNavngivenVejVersion().getEntity().toJSON());
            delveje.put(delvej);
        }
        obj.put("delveje", delveje);
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("lokalitet");
            node.addAttribute(envelope.createName("lokalitetsKode"), ""+this.getLokalitetsKode());
            node.addAttribute(envelope.createName("lokalitetsNavn"), ""+this.getLatestVersion().getLokalitetsNavn());
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
                kommunedelAfNavngivenVejEntity.getNavngivenVejVersion().getEntity().toXML(delvej, envelope);
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return node;
    }
}