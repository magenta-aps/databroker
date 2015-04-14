package dk.magenta.databroker.cvr.model.industry;

import dk.magenta.databroker.core.model.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface IndustryRepository extends EntityRepository<IndustryEntity, Integer> {
    public IndustryEntity getByCode(int code);
    public IndustryEntity getByName(String name);
}
