package dk.magenta.databroker.cvr.model.deltager.status;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface StatusRepository extends EntityRepository<StatusEntity, String> {
    public StatusEntity getByUuid(String uuid);
    public StatusEntity getByName(String name, Session session);
}
