package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "kommunedel_af_navngiven_vej_registreringsvirkning")
public class KommunedelAfNavngivenVejRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        KommunedelAfNavngivenVejEntity,
        KommunedelAfNavngivenVejRegistreringEntity,
        KommunedelAfNavngivenVejRegistreringsVirkningEntity> {

        public KommunedelAfNavngivenVejRegistreringsVirkningEntity() {
        }

        public KommunedelAfNavngivenVejRegistreringsVirkningEntity(KommunedelAfNavngivenVejRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}
