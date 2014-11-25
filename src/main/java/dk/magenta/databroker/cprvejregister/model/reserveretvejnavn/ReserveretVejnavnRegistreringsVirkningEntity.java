package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "reserveret_vejnavn_registreringsvirkning")
public class ReserveretVejnavnRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        ReserveretVejnavnEntity,
        ReserveretVejnavnVersionEntity,
        ReserveretVejnavnRegistreringsVirkningEntity> {

        public ReserveretVejnavnRegistreringsVirkningEntity(ReserveretVejnavnVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
