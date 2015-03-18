package dk.magenta.databroker.cvr.model.deltager.status;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    public StatusEntity getByUuid(String uuid);
    public StatusEntity getByName(String name);
}
