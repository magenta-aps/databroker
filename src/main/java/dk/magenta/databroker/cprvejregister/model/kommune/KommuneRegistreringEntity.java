package dk.magenta.databroker.cprvejregister.model.kommune;

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
@Table(name = "kommune_registrering")
public class KommuneRegistreringEntity
        extends DobbeltHistorikRegistrering<KommuneEntity, KommuneRegistreringEntity, KommuneRegistreringsVirkningEntity> {

        public KommuneRegistreringEntity() {
        }

        public KommuneRegistreringEntity(KommuneEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
