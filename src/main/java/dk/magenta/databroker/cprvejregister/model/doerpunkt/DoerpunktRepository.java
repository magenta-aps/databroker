package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface DoerpunktRepository extends JpaRepository<DoerpunktEntity, Long> {
}