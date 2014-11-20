package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "adgangspunkt_registreringsvirkning")
public class AdgangspunktRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        AdgangspunktEntity,
        AdgangspunktRegistreringEntity,
        AdgangspunktRegistreringsVirkningEntity> {

        public AdgangspunktRegistreringsVirkningEntity() {
        }

        public AdgangspunktRegistreringsVirkningEntity(AdgangspunktRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
