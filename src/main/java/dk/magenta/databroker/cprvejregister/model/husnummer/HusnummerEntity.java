package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
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

    public Collection<AdresseEntity> getAdresser() {
        return this.adresser;
    }


    public PostnummerEntity getPostNummer() {
        AdgangspunktEntity adgangspunktEntity = this.getAdgangspunkt();
        if (adgangspunktEntity != null) {
            AdgangspunktVersionEntity adgangspunktVersionEntity = adgangspunktEntity.getLatestVersion();
            if (adgangspunktVersionEntity != null) {
                PostnummerEntity postnummerEntity = adgangspunktVersionEntity.getLiggerIPostnummer();
                if (postnummerEntity != null) {
                    return postnummerEntity;
                }
            }
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("husnummerbetegnelse", this.getHusnummerbetegnelse());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONObject vej = this.getNavngivenVej().getLatestVersion().getEntity().toJSON();
        obj.put("vej", vej);
        PostnummerEntity postnummerEntity = this.getPostNummer();
        if (postnummerEntity != null) {
            obj.put("postnr", postnummerEntity.toJSON());
        }
        JSONArray adresser = new JSONArray();
        for (AdresseEntity adresseEntity : this.getAdresser()) {
            adresser.put(adresseEntity.toJSON());
        }
        obj.put("adresser", adresser);
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("husnr");
            node.addAttribute(envelope.createName("husnummerbetegnelse"), this.getHusnummerbetegnelse());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        this.getNavngivenVej().toXML(node, envelope);
        PostnummerEntity postnummerEntity = this.getPostNummer();
        if (postnummerEntity != null) {
            postnummerEntity.toXML(node, envelope);
        }
        return node;
    }
}
