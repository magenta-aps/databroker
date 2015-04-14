package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 29-12-14.
 */
public interface AdgangsAdresseRepository extends EntityRepository<AdgangsAdresseEntity, Long> {
    public AdgangsAdresseEntity getByUuid(String uuid);
}
