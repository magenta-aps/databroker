package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "husnummer_registreringsvirkning")
public class HusnummerRegistreringsVirkningEntity extends DobbeltHistorikVirkning<HusnummerEntity, HusnummerRegistreringEntity, HusnummerRegistreringsVirkningEntity> {

        public HusnummerRegistreringsVirkningEntity() {
        }

        public HusnummerRegistreringsVirkningEntity(HusnummerRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
