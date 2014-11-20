package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "reserveret_vejnavn_registreringsvirkning")
public class ReserveretVejnavnRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        ReserveretVejnavnEntity,
        ReserveretVejnavnRegistreringEntity,
        ReserveretVejnavnRegistreringsVirkningEntity> {

        public ReserveretVejnavnRegistreringsVirkningEntity() {
        }

        public ReserveretVejnavnRegistreringsVirkningEntity(ReserveretVejnavnRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
