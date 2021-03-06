package dk.magenta.databroker.cvr.model.deltager;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface DeltagerRepository extends JpaRepository<DeltagerEntity, Long> {

    public DeltagerEntity getByUuid(String uuid);

    public List<TransactionCallback> getBulkwireCallbacks();
    /*
    * To be implemented in interface implementation
    * */
    public void clear();

    public DeltagerEntity getByDeltagerNummer(long deltagernummer);
    public List<Long> getDeltagerNumbers();

}
