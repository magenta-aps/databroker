package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "jubk_jubk_registreringsvirkning")
public class NavngivenVejRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        NavngivenVejEntity,
        NavngivenVejRegistreringEntity,
        NavngivenVejRegistreringsVirkningEntity> {

        public NavngivenVejRegistreringsVirkningEntity() {
        }

        public NavngivenVejRegistreringsVirkningEntity(NavngivenVejRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
