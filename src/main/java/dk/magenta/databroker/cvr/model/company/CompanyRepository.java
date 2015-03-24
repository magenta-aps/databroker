package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionCallback;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public CompanyEntity getByIdentifier(String cvrNummer);
    public Collection<CompanyEntity> search(SearchParameters parameters);
    public void clear();
    public List<String> getIdentifiers();
}
