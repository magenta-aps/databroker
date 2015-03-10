package dk.magenta.databroker.correction;

import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 06-03-15.
 */
public interface CorrectionCollectionRepository extends JpaRepository<CorrectionCollectionEntity, Long> {

    public Collection<CorrectionCollectionEntity> getByFoobar(DataProviderEntity dataProviderEntity, String subregister);

}
