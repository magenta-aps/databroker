package dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "reserveret_husnr_interval_registrering")
public class ReserveretHusnrIntervalRegistreringEntity
        extends DobbeltHistorikRegistrering<ReserveretHusnrIntervalEntity, ReserveretHusnrIntervalRegistreringEntity, ReserveretHusnrIntervalRegistreringsVirkningEntity> {

        public ReserveretHusnrIntervalRegistreringEntity() {
        }

        public ReserveretHusnrIntervalRegistreringEntity(ReserveretHusnrIntervalEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
