package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adresse_registrering")
public class AdresseVersionEntity
        extends DobbeltHistorikVersion<AdresseEntity, AdresseVersionEntity, AdresseRegistreringsVirkningEntity> {

        public AdresseVersionEntity(AdresseEntity entitet) {
                super(entitet);
        }

        @Override
        protected AdresseRegistreringsVirkningEntity createVirkningEntity() {
                return new AdresseRegistreringsVirkningEntity(this);
        }
}
