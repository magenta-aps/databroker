package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

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
@Table(name = "vejnavneomraade_registrering")
public class VejnavneomraadeRegistreringEntity
        extends DobbeltHistorikRegistrering<VejnavneomraadeEntity, VejnavneomraadeRegistreringEntity, VejnavneomraadeRegistreringsVirkningEntity> {

        public VejnavneomraadeRegistreringEntity() {
        }

        public VejnavneomraadeRegistreringEntity(VejnavneomraadeEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
