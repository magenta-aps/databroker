package dk.magenta.databroker.cprvejregister.model.isopunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "isopunkt_registreringsvirkning")
public class IsoPunktRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        IsoPunktEntity,
        IsoPunktRegistreringEntity,
        IsoPunktRegistreringsVirkningEntity> {

        public IsoPunktRegistreringsVirkningEntity() {
        }

        public IsoPunktRegistreringsVirkningEntity(IsoPunktRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
