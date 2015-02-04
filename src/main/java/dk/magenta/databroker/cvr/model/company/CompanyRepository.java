package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    public CompanyEntity getByUuid(String uuid);
    public CompanyEntity getByCvrNummer(String cvrNummer);

    public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery);
    public void bulkWireReferences();

    public void flushEntities();
}
