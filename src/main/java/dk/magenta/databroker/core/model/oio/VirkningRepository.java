package dk.magenta.databroker.core.model.oio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VirkningRepository extends JpaRepository<VirkningEntity, Long> {
}
