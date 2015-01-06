package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.json.JSONObject;

import javax.persistence.*;
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
@Table(name = "adgangspunkt")
public class AdgangspunktEntity
        extends DobbeltHistorikBase<AdgangspunktEntity, AdgangspunktVersionEntity>
        implements Serializable {

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private Collection<AdgangspunktVersionEntity> versions;

    @OneToOne
    private AdgangspunktVersionEntity latestVersion;

    @OneToOne
    private AdgangspunktVersionEntity preferredVersion;



    protected AdgangspunktEntity() {
        this.versions = new ArrayList<AdgangspunktVersionEntity>();
    }

    public static AdgangspunktEntity create() {
        AdgangspunktEntity entity = new AdgangspunktEntity();
        entity.generateNewUUID();
        return entity;
    }

    @Override
    public Collection<AdgangspunktVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public AdgangspunktVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdgangspunktVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdgangspunktVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdgangspunktVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdgangspunktVersionEntity createVersionEntity() {
        return new AdgangspunktVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HusnummerEntity husnummer;

    public HusnummerEntity getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }


    //------------------------------------------------------------------------------------------------------------------


    public String getTypeName() {
        return "adgangspunkt";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        /*obj.put("postnr", this.getAdgangspunkt().getLatestVersion().getLiggerIPostnummer().getNummer());*/
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        obj.put("postnr", this.getLatestVersion().getLiggerIPostnummer().toJSON());
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("adgangspunkt");
            /*node.addAttribute(envelope.createName("postnr"), ""+this.getAdgangspunkt().getLatestVersion().getLiggerIPostnummer().getNummer());*/
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        this.getLatestVersion().getLiggerIPostnummer().toXML(node, envelope);
        /*node.addAttribute(envelope.createName("postnr"), ""+this.getAdgangspunkt().getLatestVersion().getLiggerIPostnummer().getNummer());*/
        return node;
    }
}
