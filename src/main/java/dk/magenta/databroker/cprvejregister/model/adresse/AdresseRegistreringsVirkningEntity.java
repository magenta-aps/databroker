package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "adresse_registreringsvirkning")
public class AdresseRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        AdresseEntity,
        AdresseVersionEntity,
        AdresseRegistreringsVirkningEntity> {

        public AdresseRegistreringsVirkningEntity(AdresseVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
