package dk.magenta.databroker.cprvejregister.model.vejnavneforslag;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "vejnavneforslag_registreringsvirkning")
public class VejnavneforslagRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        VejnavneforslagEntity,
        VejnavneforslagRegistreringEntity,
        VejnavneforslagRegistreringsVirkningEntity> {

        public VejnavneforslagRegistreringsVirkningEntity() {
        }

        public VejnavneforslagRegistreringsVirkningEntity(VejnavneforslagRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
