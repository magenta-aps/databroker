package dk.magenta.databroker.cprvejregister.model.doerpunkt;

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
@Table(name = "doerpunkt_registrering")
public class DoerpunktRegistreringEntity
        extends DobbeltHistorikRegistrering<DoerpunktEntity, DoerpunktRegistreringEntity, DoerpunktRegistreringsVirkningEntity> {

        public DoerpunktRegistreringEntity(DoerpunktEntity entitet) {
                super(entitet);
        }

        @Override
        protected DoerpunktRegistreringsVirkningEntity createVirkningEntity() {
                return new DoerpunktRegistreringsVirkningEntity(this);
        }
}
