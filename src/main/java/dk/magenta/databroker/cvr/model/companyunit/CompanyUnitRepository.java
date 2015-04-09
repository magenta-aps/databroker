package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.util.TransactionCallback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyUnitRepository extends JpaRepository<CompanyUnitEntity, Long> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public Collection<CompanyUnitEntity> search(SearchParameters parameters);
    public List<Long> getIdentifiers();
    public CompanyUnitEntity getByIdentifier(long pno);
    public void clear();
    public void detach(CompanyUnitEntity companyUnitEntity);
    public void detach(CompanyUnitVersionEntity companyUnitVersionEntity);
}
