package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class NavngivenVejRegistreringEntity
        extends DobbeltHistorikRegistreringEntity<NavngivenVejEntity, NavngivenVejRegistreringEntity, NavngivenVejRegistreringsVirkningEntity> {

        public NavngivenVejRegistreringEntity() {
        }

        public NavngivenVejRegistreringEntity(NavngivenVejEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
