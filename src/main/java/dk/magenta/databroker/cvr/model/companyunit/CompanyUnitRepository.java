package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyUnitRepository extends EntityRepository<CompanyUnitEntity, Long> {
    public void detach(CompanyUnitEntity companyUnitEntity);
    public void detach(CompanyUnitVersionEntity companyUnitVersionEntity);
}
