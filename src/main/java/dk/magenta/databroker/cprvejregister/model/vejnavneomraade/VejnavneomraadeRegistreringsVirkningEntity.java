package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "vejnavneomraade_registreringsvirkning")
public class VejnavneomraadeRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        VejnavneomraadeEntity,
        VejnavneomraadeRegistreringEntity,
        VejnavneomraadeRegistreringsVirkningEntity> {

        public VejnavneomraadeRegistreringsVirkningEntity() {
        }

        public VejnavneomraadeRegistreringsVirkningEntity(VejnavneomraadeRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
