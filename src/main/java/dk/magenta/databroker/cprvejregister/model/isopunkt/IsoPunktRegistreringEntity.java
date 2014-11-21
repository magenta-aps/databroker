package dk.magenta.databroker.cprvejregister.model.isopunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "isopunkt_registrering")
public class IsoPunktRegistreringEntity
        extends DobbeltHistorikRegistrering<IsoPunktEntity, IsoPunktRegistreringEntity, IsoPunktRegistreringsVirkningEntity> {

        public IsoPunktRegistreringEntity() {
        }

        public IsoPunktRegistreringEntity(IsoPunktEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}