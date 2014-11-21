package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

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
@Table(name = "adgangspunkt_registrering")
public class AdgangspunktRegistreringEntity
        extends DobbeltHistorikRegistrering<AdgangspunktEntity, AdgangspunktRegistreringEntity, AdgangspunktRegistreringsVirkningEntity> {

        public AdgangspunktRegistreringEntity(AdgangspunktEntity entitet) {
                super(entitet);
        }

        @Override
        protected AdgangspunktRegistreringsVirkningEntity createVirkningEntity() {
                return new AdgangspunktRegistreringsVirkningEntity(this);
        }
}
