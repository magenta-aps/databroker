package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "vejnavneomraade_registrering")
public class VejnavneomraadeVersionEntity
        extends DobbeltHistorikVersion<VejnavneomraadeEntity, VejnavneomraadeVersionEntity, VejnavneomraadeRegistreringsVirkningEntity> {

        public VejnavneomraadeVersionEntity(VejnavneomraadeEntity entitet) {
                super(entitet);
        }

        @Override
        protected VejnavneomraadeRegistreringsVirkningEntity createVirkningEntity() {
                return new VejnavneomraadeRegistreringsVirkningEntity(this);
        }
}
