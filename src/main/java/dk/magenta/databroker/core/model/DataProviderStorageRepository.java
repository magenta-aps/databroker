package dk.magenta.databroker.core.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-12-14.
 */
public interface DataProviderStorageRepository extends JpaRepository<DataProviderStorageEntity, Long> {
    public DataProviderStorageEntity getByOwningClass(String className);
}
