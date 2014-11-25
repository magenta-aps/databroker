package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adresse_registrering")
public class AdresseVersionEntity
        extends DobbeltHistorikVersion<AdresseEntity, AdresseVersionEntity> {

        @ManyToOne
        private AdresseEntity entitet;

        public AdresseVersionEntity() {
                super();
        }

        public AdresseVersionEntity(AdresseEntity entitet) {
                this.entitet = entitet;
        }

        @Override
        public AdresseEntity getEntitet() {
                return entitet;
        }

        @Override
        public void setEntitet(AdresseEntity entitet) {
                this.entitet = entitet;
        }
}
