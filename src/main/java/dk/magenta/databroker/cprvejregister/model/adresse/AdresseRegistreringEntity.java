package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.jubk.model.JubkEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class AdresseRegistreringEntity
        extends DobbeltHistorikRegistreringEntity<AdresseEntity, AdresseRegistreringEntity, AdresseRegistreringsVirkningEntity> {

        public AdresseRegistreringEntity() {
        }

        public AdresseRegistreringEntity(AdresseEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger) {
                super(entitet, registrering, virkninger);
        }
}
