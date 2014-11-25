package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "doerpunkt_registrering")
public class DoerpunktVersionEntity
        extends DobbeltHistorikVersion<DoerpunktEntity, DoerpunktVersionEntity> {

        @ManyToOne
        private DoerpunktEntity entitet;

        public DoerpunktVersionEntity() {
                super();
        }

        public DoerpunktVersionEntity(DoerpunktEntity entitet) {
                super(entitet);
        }

        @Override
        public DoerpunktEntity getEntitet() {
                return entitet;
        }

        @Override
        public void setEntitet(DoerpunktEntity entitet) {
                this.entitet = entitet;
        }
}
