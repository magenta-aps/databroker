package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "kommune_registreringsvirkning")
public class KommuneRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        KommuneEntity,
        KommuneVersionEntity,
        KommuneRegistreringsVirkningEntity> {

        public KommuneRegistreringsVirkningEntity(KommuneVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
