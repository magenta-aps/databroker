package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "vejnavneomraade_registrering")
public class VejnavneomraadeVersionEntity
        extends DobbeltHistorikVersion<VejnavneomraadeEntity, VejnavneomraadeVersionEntity> {

        @ManyToOne
        private VejnavneomraadeEntity entitet;

        protected VejnavneomraadeVersionEntity() {
                super();
        }

        public VejnavneomraadeVersionEntity(VejnavneomraadeEntity entitet) {
                super(entitet);
        }

        @Override
        public VejnavneomraadeEntity getEntitet() {
                return entitet;
        }

        @Override
        public void setEntitet(VejnavneomraadeEntity entitet) {
                this.entitet = entitet;
        }
}
