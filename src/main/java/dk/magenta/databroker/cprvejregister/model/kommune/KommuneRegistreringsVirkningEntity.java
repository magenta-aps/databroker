package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "kommune_registreringsvirkning")
public class KommuneRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        KommuneEntity,
        KommuneRegistreringEntity,
        KommuneRegistreringsVirkningEntity> {

        public KommuneRegistreringsVirkningEntity() {
        }

        public KommuneRegistreringsVirkningEntity(KommuneRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
