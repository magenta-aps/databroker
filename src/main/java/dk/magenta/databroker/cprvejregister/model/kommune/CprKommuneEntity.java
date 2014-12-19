package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.reserveretvejnavn.ReserveretVejnavnEntity;
import org.hibernate.annotations.Index;
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
public class CprKommuneEntity
        extends DobbeltHistorikBase<CprKommuneEntity, CprKommuneVersionEntity>
        implements Serializable, OutputFormattable {


    protected CprKommuneEntity() {
        this.versioner = new ArrayList<CprKommuneVersionEntity>();
    }

    public static CprKommuneEntity create() {
        CprKommuneEntity entity = new CprKommuneEntity();
        entity.generateNewUUID();
        return entity;
    }


    /*
    * Versioning fields
    * */

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CprKommuneVersionEntity> versioner;

    @OneToOne(fetch = FetchType.EAGER)
    private CprKommuneVersionEntity latestVersion;

    @OneToOne(fetch = FetchType.LAZY)
    private CprKommuneVersionEntity preferredVersion;


    @Override
    public Collection<CprKommuneVersionEntity> getVersioner() {
        return versioner;
    }

    @Override
    public CprKommuneVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CprKommuneVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    @Override
    public CprKommuneVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CprKommuneVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }


    /*
    * Create the relevant version entity
    * */

    @Override
    protected CprKommuneVersionEntity createVersionEntity() {
        return new CprKommuneVersionEntity(this);
    }


    /*
    * Fields on the entity
    * */

    @Basic
    @Index(name="kommuneKode")
    @Column(name = "kommunekode", nullable = false, insertable = true, updatable = true, unique = true)
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
        obj.put("navn", this.getLatestVersion().getNavn());
        obj.put("kommuneKode", this.getKommunekode());
        return obj;
    }

    public Node toXML(SOAPElement parent, SOAPEnvelope envelope) {
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

}
