package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.util.TransactionCallback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface DeltagerRepository extends JpaRepository<DeltagerEntity, Long> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public List<Long> getIdentifiers();
    public DeltagerEntity getByIdentifier(long deltagernummer);
    public void clear();
    public void detach(DeltagerEntity deltagerEntity);
    public void detach(DeltagerVersionEntity deltagerVersionEntity);
}
