package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface VejstykkeRepository extends EntityRepository<VejstykkeEntity, Integer> {
    public VejstykkeEntity getByUuid(String uuid);
}
