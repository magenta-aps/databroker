package dk.magenta.databroker.cprvejregister.model.isopunkt;

import dk.magenta.databroker.cprvejregister.model.vejnavneomraade.VejnavneomraadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface IsoPunktRepository extends JpaRepository<IsoPunktEntity, Long> {
}