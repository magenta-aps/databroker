package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "adgangspunkt_registreringsvirkning")
public class AdgangspunktRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        AdgangspunktEntity,
        AdgangspunktRegistreringEntity,
        AdgangspunktRegistreringsVirkningEntity> {

        public AdgangspunktRegistreringsVirkningEntity() {
        }

        public AdgangspunktRegistreringsVirkningEntity(AdgangspunktRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
