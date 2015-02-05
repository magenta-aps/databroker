package dk.magenta.databroker.cvr.model.deltager.type;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    public TypeEntity getByUuid(String uuid);
    public TypeEntity getByName(String name);
}
