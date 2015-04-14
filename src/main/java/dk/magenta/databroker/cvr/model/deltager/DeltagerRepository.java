package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.model.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface DeltagerRepository extends EntityRepository<DeltagerEntity, Long> {
    public void detach(DeltagerEntity deltagerEntity);
    public void detach(DeltagerVersionEntity deltagerVersionEntity);
}
