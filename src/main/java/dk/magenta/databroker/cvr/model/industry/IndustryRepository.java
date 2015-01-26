package dk.magenta.databroker.cvr.model.industry;

import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface IndustryRepository extends JpaRepository<IndustryEntity, Long> {
    public IndustryEntity getByCode(int code);
    public IndustryEntity getByName(String name);
}
