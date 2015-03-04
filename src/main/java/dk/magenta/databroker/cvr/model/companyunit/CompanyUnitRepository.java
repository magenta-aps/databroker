package dk.magenta.databroker.cvr.model.companyunit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyUnitRepository extends JpaRepository<CompanyUnitEntity, Long> {

    public CompanyUnitEntity getByUuid(String uuid);


    /*
    * To be implemented in interface implementation
    * */
    //public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery);
    public void bulkWireReferences();
    public void clear();
    public CompanyUnitEntity getByPno(long pno);
}
