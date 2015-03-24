package dk.magenta.databroker.cvr.model.deltager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface DeltagerRepository extends JpaRepository<DeltagerEntity, Long> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public List<Long> getIdentifiers();
    public DeltagerEntity getByIdentifier(long deltagernummer);
    public void clear();
}
