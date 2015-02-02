package dk.magenta.databroker.cvr.model.companyunit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by lars on 02-02-15.
 */


interface CompanyUnitRepositoryCustom {
    public void bulkWireReferences();
}

public class CompanyUnitRepositoryImpl implements CompanyUnitRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void bulkWireReferences() {
        System.out.println("Updating references");
        Query query = this.entityManager.createNativeQuery("update cvr_companyunit unit join cvr_company company on unit.cvr_nummer=company.cvr_nummer set unit.company_id=company.id");
        query.executeUpdate();
        System.out.println("References updated");
        //update cvr_companyunit unit join cvr_company company on unit.cvr_nummer=company.cvr_nummer set unit.company_id=company.id;
    }

}
