package dk.magenta.databroker.cvr.model.form;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyFormRepository extends EntityRepository<CompanyFormEntity, Integer> {
    public CompanyFormEntity getByCode(int code);
    public CompanyFormEntity getByName(String name);
}
