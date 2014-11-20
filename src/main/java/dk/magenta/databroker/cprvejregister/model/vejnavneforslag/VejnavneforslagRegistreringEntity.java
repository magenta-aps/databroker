package dk.magenta.databroker.cprvejregister.model.vejnavneforslag;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "vejnavneforslag_registrering")
public class VejnavneforslagRegistreringEntity
        extends DobbeltHistorikRegistreringEntity<VejnavneforslagEntity, VejnavneforslagRegistreringEntity, VejnavneforslagRegistreringsVirkningEntity> {

        public VejnavneforslagRegistreringEntity() {
        }

        public VejnavneforslagRegistreringEntity(VejnavneforslagEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
