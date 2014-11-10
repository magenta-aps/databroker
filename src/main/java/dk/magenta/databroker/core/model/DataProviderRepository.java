package dk.magenta.databroker.core.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jubk on 06-11-2014.
 */

@Repository
public interface DataProviderRepository extends JpaRepository<DataProviderEntity, Long> {
}
