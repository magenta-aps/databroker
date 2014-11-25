package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adgangspunkt_registrering")
public class AdgangspunktVersionEntity
        extends DobbeltHistorikVersion<AdgangspunktEntity, AdgangspunktVersionEntity> {

        @ManyToOne
        private AdgangspunktEntity entitet;

        protected AdgangspunktVersionEntity() {
                super();
        }

        public AdgangspunktVersionEntity(AdgangspunktEntity entitet) {
                super(entitet);
        }

        @Override
        public AdgangspunktEntity getEntitet() {
                return entitet;
        }

        @Override
        public void setEntitet(AdgangspunktEntity entitet) {
                this.entitet = entitet;
        }
}
