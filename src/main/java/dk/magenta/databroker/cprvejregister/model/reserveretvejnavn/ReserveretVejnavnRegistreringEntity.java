package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "reserveret_vejnavn_registrering")
public class ReserveretVejnavnRegistreringEntity
        extends DobbeltHistorikRegistreringEntity<ReserveretVejnavnEntity, ReserveretVejnavnRegistreringEntity, ReserveretVejnavnRegistreringsVirkningEntity> {

        public ReserveretVejnavnRegistreringEntity() {
        }

        public ReserveretVejnavnRegistreringEntity(ReserveretVejnavnEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
