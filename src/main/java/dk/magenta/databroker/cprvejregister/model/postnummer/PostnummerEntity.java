package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
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
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "postnummer")
public class PostnummerEntity
        extends DobbeltHistorikBase<PostnummerEntity, PostnummerVersionEntity>
        implements Serializable, OutputFormattable {


    protected PostnummerEntity() {
        this.versioner = new ArrayList<PostnummerVersionEntity>();
    }

    public static PostnummerEntity create() {
        PostnummerEntity entity = new PostnummerEntity();
        entity.generateNewUUID();
        return entity;
    }

    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<PostnummerVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER)
    private PostnummerVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private PostnummerVersionEntity preferredVersion;

    @Override
    public Collection<PostnummerVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public PostnummerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(PostnummerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public PostnummerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(PostnummerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

     @Override
    protected PostnummerVersionEntity createVersionEntity() {
        return new PostnummerVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Column(nullable = false, insertable = true, updatable = true)
    @Index(name="nummer")
    private int nummer;

    @OneToMany(mappedBy = "liggerIPostnummer", fetch = FetchType.LAZY)
    private Collection<AdgangspunktVersionEntity> adgangspunkter;

    public int getNummer() {
        return this.nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public Collection<AdgangspunktVersionEntity> getAdgangspunkter() {
        return this.adgangspunkter;
    }

    public void setAdgangspunkter(Collection<AdgangspunktVersionEntity> adgangspunkter) {
        this.adgangspunkter = adgangspunkter;
    }





    //------------------------------------------------------------------------------------------------------------------


    public String getTypeName() {
        return "postnummer";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getUuid());
        obj.put("navn", this.getLatestVersion().getNavn());
        obj.put("postnummer", this.getNummer());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        return obj;
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("vej");
            node.addAttribute(envelope.createName("navn"), this.getLatestVersion().getNavn());
            node.addAttribute(envelope.createName("postnummer"), ""+this.getNummer());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        SOAPElement node = this.toXML(parent, envelope);
        return node;
    }
}
