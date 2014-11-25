package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "husnummer_registreringsvirkning")
public class HusnummerRegistreringsVirkningEntity extends DobbeltHistorikVirkning<HusnummerEntity, HusnummerVersionEntity, HusnummerRegistreringsVirkningEntity> {

        public HusnummerRegistreringsVirkningEntity(HusnummerVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
