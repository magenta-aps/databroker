package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
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
@Table(name = "husnummer")
public class HusnummerEntity
        extends UniqueBase
        implements Serializable, OutputFormattable {

    public HusnummerEntity() {
    }

    public static HusnummerEntity create() {
        HusnummerEntity entity = new HusnummerEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Fields on the entity
    * */

    @OneToOne(mappedBy = "husnummer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AdgangspunktEntity adgangspunkt;

    @OneToMany(mappedBy = "husnummer", fetch = FetchType.LAZY)
    private Collection<AdresseEntity> adresser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private NavngivenVejEntity navngivenVej;

    @Basic
    @Column(nullable = true)
    @Index(name = "husnummerbetegnelse")
    private String husnummerbetegnelse;

    public AdgangspunktEntity getAdgangspunkt() {
        return adgangspunkt;
    }

    public void setAdgangspunkt(AdgangspunktEntity adgangspunkt) {
        this.adgangspunkt = adgangspunkt;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return this.navngivenVej;
    }

    public void setNavngivenVej(NavngivenVejEntity navngivenVej) {
        this.navngivenVej = navngivenVej;
    }

    public String getHusnummerbetegnelse() {
        return husnummerbetegnelse;
    }

    public void setHusnummerbetegnelse(String husnummerbetegnelse) {
        this.husnummerbetegnelse = husnummerbetegnelse;
    }

    //------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("vejnavn", this.getNavngivenVej().getLatestVersion().getVejnavn());
        obj.put("husnr", this.getHusnummerbetegnelse());
        /*obj.put("postnr", this.getAdgangspunkt().getLatestVersion().getLiggerIPostnummer().getNummer());*/
        return obj;
    }

    public Node toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("vej");
            node.addAttribute(envelope.createName("vejnavn"), this.getNavngivenVej().getLatestVersion().getVejnavn());
            node.addAttribute(envelope.createName("husnr"), this.getHusnummerbetegnelse());
            /*node.addAttribute(envelope.createName("postnr"), ""+this.getAdgangspunkt().getLatestVersion().getLiggerIPostnummer().getNummer());*/
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
