package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "navngiven_vej_registreringsvirkning")
public class NavngivenVejRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        NavngivenVejEntity,
        NavngivenVejVersionEntity,
        NavngivenVejRegistreringsVirkningEntity> {

        public NavngivenVejRegistreringsVirkningEntity(NavngivenVejVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
