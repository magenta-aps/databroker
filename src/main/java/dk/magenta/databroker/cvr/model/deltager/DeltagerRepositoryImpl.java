package dk.magenta.databroker.cvr.model.deltager;

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

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void bulkWireReferences() {
        System.out.println("Updating references");
        Query query = this.entityManager.createNativeQuery("update cvr_deltager deltager join cvr_company company on deltager.cvr_nummer=company.cvr_nummer set deltager.company_id=company.id");
        query.executeUpdate();
        System.out.println("References updated");
    }

    public void clear() {
        if (this.entityManager != null) {
            System.out.println("clearing " + this.entityManager);
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

}
