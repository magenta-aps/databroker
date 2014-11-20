package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface ReserveretVejnavnRepository extends JpaRepository<ReserveretVejnavnEntity, Long> {
}