package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
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
        extends DobbeltHistorikRegistreringEntity<AdgangspunktEntity, AdgangspunktRegistreringEntity, AdgangspunktRegistreringsVirkningEntity> {

        public AdgangspunktRegistreringEntity() {
        }

        public AdgangspunktRegistreringEntity(AdgangspunktEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
