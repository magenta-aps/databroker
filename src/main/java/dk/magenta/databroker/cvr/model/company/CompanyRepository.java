package dk.magenta.databroker.cvr.model.company;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    public CompanyEntity getByUuid(String uuid);
    public CompanyEntity getByCvrNummer(String cvrNummer);

}
