package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by lars on 26-01-15.
 */

@Entity
@Table(name = "cvr_company")
public class CompanyEntity extends DobbeltHistorikBase<CompanyEntity, CompanyVersionEntity> implements OutputFormattable {

    public CompanyEntity() {

        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompanyVersionEntity> versioner;

    @Override
    public Collection<CompanyVersionEntity> getVersioner() {
        return versioner;
    }

    public void setVersioner(Collection<CompanyVersionEntity> versioner) {
        this.versioner = versioner;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CompanyVersionEntity latestVersion;

    @Override
    public CompanyVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CompanyVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private CompanyVersionEntity preferredVersion;

    @Override
    public CompanyVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CompanyVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected CompanyVersionEntity createVersionEntity() {
        return new CompanyVersionEntity(this);
    }


    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column(nullable = false)
    @Index(name = "cvrNummer")
    private String cvrNummer;

    public String getCvrNummer() {
        return cvrNummer;
    }

    public void setCvrNummer(String cvrNummer) {
        this.cvrNummer = cvrNummer;
    }


    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "company";
    }

}
