package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.core.model.EntityRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface LokalitetRepository extends EntityRepository<LokalitetEntity, Long> {

    public LokalitetEntity getByUuid(String uuid);

    /*
    * To be implemented in interface implementation
    * */
    public LokalitetEntity getByKommunekodeAndLokalitetsnavn(int kommuneKode, String lokalitetsnavn);
}
