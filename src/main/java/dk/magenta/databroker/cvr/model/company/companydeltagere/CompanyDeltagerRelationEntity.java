package dk.magenta.databroker.cvr.model.company.companydeltagere;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lars on 18-03-15.
 */
@Entity
@Table(name = "cvr_company_deltager_relation")
public class CompanyDeltagerRelationEntity extends UniqueBase {

    public CompanyDeltagerRelationEntity() {
        this.generateNewUUID();
    }

    @ManyToOne
    private CompanyVersionEntity companyVersionEntity;

    public CompanyVersionEntity getCompanyVersionEntity() {
        return companyVersionEntity;
    }

    public void setCompanyVersionEntity(CompanyVersionEntity companyVersionEntity) {
        this.companyVersionEntity = companyVersionEntity;
    }

    @Column
    private long deltagerNummer;

    public long getDeltagerNummer() {
        return deltagerNummer;
    }

    public void setDeltagerNummer(long deltagerNummer) {
        this.deltagerNummer = deltagerNummer;
    }

    @Column
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    @Override
    public String getTypeName() {
        return "companyDeltagerRelation";
    }
}
