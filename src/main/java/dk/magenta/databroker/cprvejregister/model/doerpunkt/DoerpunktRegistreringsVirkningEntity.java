package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "doerpunkt_registreringsvirkning")
public class DoerpunktRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        DoerpunktEntity,
        DoerpunktRegistreringEntity,
        DoerpunktRegistreringsVirkningEntity> {

        public DoerpunktRegistreringsVirkningEntity() {
        }

        public DoerpunktRegistreringsVirkningEntity(DoerpunktRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
