package dk.magenta.databroker.cvr.model.deltager.type;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface TypeRepository extends EntityRepository<TypeEntity, String> {
    public TypeEntity getByUuid(String uuid);
    public TypeEntity getByName(String name, Session session);
}
