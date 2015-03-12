package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    public CompanyEntity getByUuid(String uuid);
    public CompanyEntity getByCvr(String cvrNummer);
    public Collection<CompanyEntity> search(SearchParameters parameters);
    public void clear();
    public List<String> getCvrNumbers();
}
