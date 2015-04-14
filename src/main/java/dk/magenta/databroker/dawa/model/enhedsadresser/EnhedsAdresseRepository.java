package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 29-12-14.
 */
public interface EnhedsAdresseRepository extends EntityRepository<EnhedsAdresseEntity, String> {
    public EnhedsAdresseEntity getByUuid(String uuid);
}
