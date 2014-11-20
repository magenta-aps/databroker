package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class KommunedelAfNavngivenVejRegistreringEntity
        extends DobbeltHistorikRegistreringEntity<KommunedelAfNavngivenVejEntity, KommunedelAfNavngivenVejRegistreringEntity, KommunedelAfNavngivenVejRegistreringsVirkningEntity> {

        public KommunedelAfNavngivenVejRegistreringEntity() {
        }

        public KommunedelAfNavngivenVejRegistreringEntity(KommunedelAfNavngivenVejEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
