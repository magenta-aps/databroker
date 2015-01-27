package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 26-01-15.
 */

@Entity
@Table(name = "cvr_companyunit")
public class CompanyUnitEntity extends DobbeltHistorikBase<CompanyUnitEntity, CompanyUnitVersionEntity> implements OutputFormattable {

    public CompanyUnitEntity() {
        this.versions = new ArrayList<CompanyUnitVersionEntity>();
        this.generateNewUUID();
    }

    //------------------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompanyUnitVersionEntity> versions;

    @Override
    public Collection<CompanyUnitVersionEntity> getVersioner() {
        return versions;
    }

    public void setVersions(Collection<CompanyUnitVersionEntity> versions) {
        this.versions = versions;
    }

    //----------------------------------------------------

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CompanyUnitVersionEntity latestVersion;

    @Override
    public CompanyUnitVersionEntity getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(CompanyUnitVersionEntity latestVersion) {
        this.latestVersion = latestVersion;
    }

    //----------------------------------------------------

    @OneToOne
    private CompanyUnitVersionEntity preferredVersion;

    @Override
    public CompanyUnitVersionEntity getPreferredVersion() {
        return preferredVersion;
    }

    @Override
    public void setPreferredVersion(CompanyUnitVersionEntity preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    //----------------------------------------------------

    @Override
    protected CompanyUnitVersionEntity createVersionEntity() {
        return new CompanyUnitVersionEntity(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */

    @Column(nullable = false)
    @Index(name = "pno")
    private long pno;

    public long getPNO() {
        return pno;
    }

    public void setPNO(long pno) {
        this.pno = pno;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTypeName() {
        return "companyunit";
    }

}
