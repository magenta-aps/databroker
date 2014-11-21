package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adresse_registrering")
public class AdresseRegistreringEntity
        extends DobbeltHistorikRegistrering<AdresseEntity, AdresseRegistreringEntity, AdresseRegistreringsVirkningEntity> {

        public AdresseRegistreringEntity(AdresseEntity entitet) {
                super(entitet);
        }

        @Override
        protected AdresseRegistreringsVirkningEntity createVirkningEntity() {
                return new AdresseRegistreringsVirkningEntity(this);
        }
}
