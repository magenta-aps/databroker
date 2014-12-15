package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval.ReserveretHusnrIntervalEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
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
    private KommuneEntity kommune;

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

    public KommuneEntity getKommune() {
        return this.kommune;
    }

    public void setKommune(KommuneEntity kommune) {
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


    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("vejKode", this.getVejkode());
        obj.put("kommuneKode", this.getKommune().getKommunekode());
        return obj;
    }

    public Node toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("delvej");
            node.addAttribute(envelope.createName("vejKode"), ""+this.getVejkode());
            node.addAttribute(envelope.createName("kommuneKode"), ""+this.getKommune().getKommunekode());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
