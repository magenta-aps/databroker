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
        this.entityManager.createNativeQuery("update cvr_companyunit unit join cvr_company company on unit.cvr_nummer=company.cvr_nummer set unit.company_id=company.id").executeUpdate();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unit join dawa_enhedsadresse_version address on unit.address_descriptor=address.descriptor set unit.address=address.entity.id").executeUpdate();
        System.out.println("References updated");
    }

}
