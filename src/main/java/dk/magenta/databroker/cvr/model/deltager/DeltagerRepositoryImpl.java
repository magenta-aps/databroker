package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.util.Util;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by lars on 02-02-15.
 */


interface DeltagerRepositoryCustom {
    public void bulkWireReferences();
    public void clear();
}

public class DeltagerRepositoryImpl implements DeltagerRepositoryCustom {

    private EntityManager entityManager;

    private Logger log = Logger.getLogger(DeltagerRepository.class);

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void bulkWireReferences() {
        this.log.info("Updating references between members and companies");
        long time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_deltager deltager join cvr_company company on deltager.cvr_nummer=company.cvr_nummer set deltager.company_id=company.id").executeUpdate();
        this.log.info("References updated in "+(Util.getTime()-time)+" ms");
    }

    public void clear() {
        if (this.entityManager != null) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

}
