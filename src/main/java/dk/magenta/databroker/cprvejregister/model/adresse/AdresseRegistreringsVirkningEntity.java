package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.jubk.model.JubkEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "adresse_registreringsvirkning")
public class AdresseRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        AdresseEntity,
        AdresseRegistreringEntity,
        AdresseRegistreringsVirkningEntity> {

        public AdresseRegistreringsVirkningEntity() {
        }

        public AdresseRegistreringsVirkningEntity(AdresseRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
