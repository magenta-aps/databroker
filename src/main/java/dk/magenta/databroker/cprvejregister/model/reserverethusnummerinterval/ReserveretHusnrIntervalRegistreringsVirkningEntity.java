package dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "reserveret_husnr_interval_registreringsvirkning")
public class ReserveretHusnrIntervalRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        ReserveretHusnrIntervalEntity,
        ReserveretHusnrIntervalRegistreringEntity,
        ReserveretHusnrIntervalRegistreringsVirkningEntity> {

        public ReserveretHusnrIntervalRegistreringsVirkningEntity() {
        }

        public ReserveretHusnrIntervalRegistreringsVirkningEntity(ReserveretHusnrIntervalRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
