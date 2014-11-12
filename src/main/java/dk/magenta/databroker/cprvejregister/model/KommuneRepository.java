package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.testmodel.TestAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lars on 11-11-14.
 */
@Repository
public interface KommuneRepository extends JpaRepository<KommuneEntity, Long> {
}