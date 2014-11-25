package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "vejnavneomraade_registreringsvirkning")
public class VejnavneomraadeRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        VejnavneomraadeEntity,
        VejnavneomraadeVersionEntity,
        VejnavneomraadeRegistreringsVirkningEntity> {

        public VejnavneomraadeRegistreringsVirkningEntity(VejnavneomraadeVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
