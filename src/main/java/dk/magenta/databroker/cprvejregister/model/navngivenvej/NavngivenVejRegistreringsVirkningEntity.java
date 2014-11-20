package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "navngiven_vej_registreringsvirkning")
public class NavngivenVejRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        NavngivenVejEntity,
        NavngivenVejRegistreringEntity,
        NavngivenVejRegistreringsVirkningEntity> {

        public NavngivenVejRegistreringsVirkningEntity() {
        }

        public NavngivenVejRegistreringsVirkningEntity(NavngivenVejRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
