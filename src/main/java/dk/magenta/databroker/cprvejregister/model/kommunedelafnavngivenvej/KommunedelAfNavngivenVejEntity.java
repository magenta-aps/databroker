package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval.ReserveretHusnrIntervalEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.*;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "kommunedel_af_navngiven_vej")
public class KommunedelAfNavngivenVejEntity
        extends UniqueBase
        implements Serializable, OutputFormattable {

    public KommunedelAfNavngivenVejEntity() {

    }

    public static KommunedelAfNavngivenVejEntity create() {
        KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity = new KommunedelAfNavngivenVejEntity();
        kommunedelAfNavngivenVejEntity.generateNewUUID();
        return kommunedelAfNavngivenVejEntity;
    }

    /*
    * Fields on the entity
    * */

    @Basic
    @Column(name = "vejkode", nullable = false, insertable = true, updatable = true)
    @Index(name = "vejkode")
    private int vejkode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "navngiven_vej_registrering_id", nullable = false)
    private NavngivenVejVersionEntity navngivenVejVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kommune_id", nullable = false)
    private CprKommuneEntity kommune;

    @OneToMany(mappedBy = "kommunedelAfNavngivenVej", fetch = FetchType.LAZY)
    private Collection<ReserveretHusnrIntervalEntity> reserveredeHusnrIntervaller;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private LokalitetEntity lokalitet;


    public int getVejkode() {
        return this.vejkode;
    }

    public void setVejkode(int vejkode) {
        this.vejkode = vejkode;
    }

    public NavngivenVejVersionEntity getNavngivenVejVersion() {
        return navngivenVejVersion;
    }

    public void setNavngivenVejVersion(NavngivenVejVersionEntity navngivenVejRegistrering) {
        this.navngivenVejVersion = navngivenVejRegistrering;
    }

    public CprKommuneEntity getKommune() {
        return this.kommune;
    }

    public void setKommune(CprKommuneEntity kommune) {
        this.kommune = kommune;
    }

    public Collection<ReserveretHusnrIntervalEntity> getReserveredeHusnrIntervalller() {
        return this.reserveredeHusnrIntervaller;
    }

    public void setReserveredeHusnrIntervalller(Collection<ReserveretHusnrIntervalEntity> reserveredeHusnrIntervalller) {
        this.reserveredeHusnrIntervaller = reserveredeHusnrIntervalller;
    }

    public LokalitetEntity getLokalitet() {
        return this.lokalitet;
    }
    public void setLokalitet(LokalitetEntity lokalitet) {
        this.lokalitet = lokalitet;
    }



    //------------------------------------------------------------------------------------------------------------------


    public String getTypeName() {
        return "kommunedelAfNavngivenVej";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("vejKode", this.getVejkode());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        obj.put("kommune", this.getKommune().toJSON());
        obj.put("vej", this.getNavngivenVejVersion().getEntity().toJSON());
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("delvej");
            node.addAttribute(envelope.createName("vejKode"), ""+this.getVejkode());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        this.getKommune().toXML(node, envelope);
        return node;
    }
}
