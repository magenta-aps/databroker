package dk.magenta.databroker.core.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by jubk on 06-11-2014.
 */

@Repository
public interface DataProviderRepository extends JpaRepository<DataProviderEntity, Long> {

    public DataProviderEntity getFirstByType(String name);

    public Collection<DataProviderEntity> getByType(String type);

    public DataProviderEntity getByUuid(String uuid);

    public DataProviderEntity getByName(String name);

}
