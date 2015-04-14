package dk.magenta.databroker.cvr.model.deltager.rolle;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface RolleRepository extends EntityRepository<RolleEntity, String> {
    public RolleEntity getByUuid(String uuid);
    public RolleEntity getByName(String name, Session session);
}
