package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.util.Util;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by lars on 02-02-15.
 */


interface CompanyUnitRepositoryCustom {
    public void bulkWireReferences();
    public void clear();
}

public class CompanyUnitRepositoryImpl implements CompanyUnitRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    private Logger log = Logger.getLogger(CompanyUnitRepositoryImpl.class);

    public void bulkWireReferences() {
        this.log.info("Updating references between units and companies");
        long time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit unit join cvr_company company on unit.cvr_nummer=company.cvr_nummer set unit.company_id=company.id").executeUpdate();
        this.log.info("References updated in " + (Util.getTime() - time) + " ms");
        this.log.info("Updating references between units and addresses");
        time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unit join dawa_enhedsadresse address on unit.address_descriptor=address.descriptor set unit.address_id=address.id").executeUpdate();
        this.log.info("References updated in "+(Util.getTime() - time)+" ms");
    }


    public void clear() {
        if (this.entityManager != null) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

}
