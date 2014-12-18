package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.register.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
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
@Table(name = "adresse")
public class AdresseEntity
        extends DobbeltHistorikBase<AdresseEntity, AdresseVersionEntity>
        implements Serializable, OutputFormattable {

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private Collection<AdresseVersionEntity> versions;

    @OneToOne
    private AdresseVersionEntity latestVersion;

    @OneToOne
    private AdresseVersionEntity preferredVersion;



    protected AdresseEntity() {
        this.versions = new ArrayList<AdresseVersionEntity>();
    }

    public static AdresseEntity create() {
        AdresseEntity entity = new AdresseEntity();
        entity.generateNewUUID();
        return entity;
    }

    @Override
    public Collection<AdresseVersionEntity> getVersioner() {
        return versions;
    }

    @Override
    public AdresseVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(AdresseVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public AdresseVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(AdresseVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    @Override
    protected AdresseVersionEntity createVersionEntity() {
        return new AdresseVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private HusnummerEntity husnummer;

    @OneToOne(fetch = FetchType.LAZY)
    private DoerpunktEntity doerPunkt;

    public HusnummerEntity getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(HusnummerEntity husnummer) {
        this.husnummer = husnummer;
    }

    public DoerpunktEntity getDoerPunkt() {
        return doerPunkt;
    }

    public void setDoerPunkt(DoerpunktEntity doerPunkt) {
        this.doerPunkt = doerPunkt;
    }

    public NavngivenVejEntity getNavngivenVej() {
        return this.getHusnummer().getNavngivenVej();
    }

    public PostnummerEntity getPostnummer() {
        return this.getHusnummer().getPostNummer();
    }


    public String getAdresseBetegnelse() {
        StringList stringList = new StringList();
        stringList.append(this.getNavngivenVej().getLatestVersion().getVejnavn());
        stringList.append(" "+this.getHusnummer().getHusnummerbetegnelse());
        String etage = this.getLatestVersion().getEtageBetegnelse();
        if (etage != null && !etage.isEmpty()) {
            stringList.append(", "+etage+".");
        }
        String doer = this.getLatestVersion().getDoerBetegnelse();
        if (doer != null && !doer.isEmpty()) {
            stringList.append(" "+doer);
        }

        PostnummerEntity postnummerEntity = this.getPostnummer();
        if (postnummerEntity != null) {
            stringList.append(", "+postnummerEntity.getNummer()+" ");
            stringList.append(postnummerEntity.getLatestVersion().getNavn());
        }
        return stringList.join().trim();
    }

    //------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("etage", this.getLatestVersion().getEtageBetegnelse());
        obj.put("doer", this.getLatestVersion().getDoerBetegnelse());
        obj.put("adressebetegnelse", this.getAdresseBetegnelse());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        obj.put("husnr", this.getHusnummer().toJSON());
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("adresse");
            node.addAttribute(envelope.createName("etage"), this.getLatestVersion().getEtageBetegnelse());
            node.addAttribute(envelope.createName("doer"), this.getLatestVersion().getDoerBetegnelse());
            node.addAttribute(envelope.createName("adressebetegnelse"), this.getAdresseBetegnelse());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }
    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        this.getHusnummer().toXML(node, envelope);
        return node;
    }
}
