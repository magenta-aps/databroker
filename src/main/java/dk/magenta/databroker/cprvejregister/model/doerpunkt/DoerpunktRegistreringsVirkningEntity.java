package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "doerpunkt_registreringsvirkning")
public class DoerpunktRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        DoerpunktEntity,
        DoerpunktRegistreringEntity,
        DoerpunktRegistreringsVirkningEntity> {

        public DoerpunktRegistreringsVirkningEntity() {
        }

        public DoerpunktRegistreringsVirkningEntity(DoerpunktRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
