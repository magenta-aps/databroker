package dk.magenta.databroker.cprvejregister.model.lokalitet;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import org.hibernate.annotations.Index;
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
 * Created by lars on 12/12/14.
 */
@Entity
@Table(name = "lokalitet")
public class LokalitetEntity
        extends DobbeltHistorikBase<LokalitetEntity, LokalitetVersionEntity>
        implements Serializable, OutputFormattable {

    public LokalitetEntity() {
        this.versioner = new ArrayList<LokalitetVersionEntity>();
    }

    public static LokalitetEntity create() {
        LokalitetEntity lokalitetEntity = new LokalitetEntity();
        lokalitetEntity.generateNewUUID();
        return lokalitetEntity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<LokalitetVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private LokalitetVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private LokalitetVersionEntity preferredVersion;

    @Override
    public Collection<LokalitetVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public LokalitetVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(LokalitetVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public LokalitetVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(LokalitetVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }



    @Override
    protected LokalitetVersionEntity createVersionEntity() {
        return new LokalitetVersionEntity(this);
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


    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("lokalitetsKode", this.getLokalitetsKode());
        obj.put("lokalitetsNavn", this.getLatestVersion().getLokalitetsNavn());
        return obj;
    }

    public Node toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("delvej");
            node.addAttribute(envelope.createName("lokalitetsKode"), ""+this.getLokalitetsKode());
            node.addAttribute(envelope.createName("lokalitetsNavn"), ""+this.getLatestVersion().getLokalitetsNavn());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
