package dk.magenta.databroker.cvr.model.deltager.rolle;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface RolleRepository extends JpaRepository<RolleEntity, Long> {
    public RolleEntity getByUuid(String uuid);
    public RolleEntity getByName(String name);
}
