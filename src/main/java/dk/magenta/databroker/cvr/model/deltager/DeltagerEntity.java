package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.util.cache.Cacheable;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 26-01-15.
 */

@Entity
@Table(name = "cvr_deltager")
public class DeltagerEntity extends DobbeltHistorikBase<DeltagerEntity, DeltagerVersionEntity> implements OutputFormattable, Cacheable {

    public DeltagerEntity() {
        this.versions = new ArrayList<DeltagerVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Column
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<DeltagerVersionEntity> versions;

    @Override
    public Collection<DeltagerVersionEntity> getVersions() {
        return versions;
    }

    public void setVersions(Collection<DeltagerVersionEntity> versions) {
        this.versions = versions;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DeltagerVersionEntity latestVersion;

    @Override
    public DeltagerVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(DeltagerVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private DeltagerVersionEntity preferredVersion;

    @Override
    public DeltagerVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(DeltagerVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected DeltagerVersionEntity createVersionEntity() {
        return new DeltagerVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column(nullable = false)
    @Index(name = "deltagerNummerIndex")
    private long deltagerNummer;

    public long getDeltagerNummer() {
        return deltagerNummer;
    }

    public void setDeltagerNummer(long deltagerNummer) {
        this.deltagerNummer = deltagerNummer;
    }

//------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "deltager";
    }

    public JSONObject toJSON() {
        return this.toJSON(this.latestVersion);
    }

    public JSONObject toJSON(DeltagerVersionEntity version) {
        JSONObject obj = super.toJSON();

        obj.put("participantNumber", this.deltagerNummer);
        obj.put("name", version.getName());
        obj.put("validFrom", version.getValidFrom());
        obj.put("role", version.getRolle().toJSON());
        obj.put("type", version.getType().toJSON());
        obj.put("status", version.getStatus().toJSON());
        obj.put("location", version.getLocationAddress().toJSON());

        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "deltager";

    public static String joinCompany() {
        return databaseKey+".company as "+CompanyEntity.databaseKey;
    }

    public static Condition nummerCondition(long deltagerNummer) {
        return RepositoryUtil.whereField(deltagerNummer, databaseKey + ".deltagerNummer", null);
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { ""+this.getDeltagerNummer() };
    }
}
