package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "postnummer_registreringsvirkning")
public class PostnummerRegistreringsVirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        PostnummerEntity,
        PostnummerRegistreringEntity,
        PostnummerRegistreringsVirkningEntity> {

        public PostnummerRegistreringsVirkningEntity() {
        }

        public PostnummerRegistreringsVirkningEntity(PostnummerRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
