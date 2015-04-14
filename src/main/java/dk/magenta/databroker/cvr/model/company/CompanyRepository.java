package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyRepository extends EntityRepository<CompanyEntity, Long> {
    public void detach(CompanyEntity companyEntity);
    public void detach(CompanyVersionEntity companyVersionEntity);
}
