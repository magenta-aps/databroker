package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.model.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface PostNummerRepository extends EntityRepository<PostNummerEntity, Integer> {
    public PostNummerEntity getByUuid(String uuid);
    public PostNummerEntity getByNr(int nr);
}
