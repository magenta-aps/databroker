package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

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
@Table(name = "reserveret_vejnavn_registrering")
public class ReserveretVejnavnRegistreringEntity
        extends DobbeltHistorikRegistrering<ReserveretVejnavnEntity, ReserveretVejnavnRegistreringEntity, ReserveretVejnavnRegistreringsVirkningEntity> {

        public ReserveretVejnavnRegistreringEntity() {
        }

        public ReserveretVejnavnRegistreringEntity(ReserveretVejnavnEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
