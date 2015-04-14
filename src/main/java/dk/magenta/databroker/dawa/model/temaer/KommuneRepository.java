package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface KommuneRepository extends EntityRepository<KommuneEntity, Integer> {
    public KommuneEntity getByKode(int kommunekode);
}
