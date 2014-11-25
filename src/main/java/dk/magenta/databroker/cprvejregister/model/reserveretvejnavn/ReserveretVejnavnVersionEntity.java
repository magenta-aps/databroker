package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "reserveret_vejnavn_registrering")
public class ReserveretVejnavnVersionEntity
        extends DobbeltHistorikVersion<ReserveretVejnavnEntity, ReserveretVejnavnVersionEntity, ReserveretVejnavnRegistreringsVirkningEntity> {

        public ReserveretVejnavnVersionEntity(ReserveretVejnavnEntity entitet) {
                super(entitet);
        }

        @Override
        protected ReserveretVejnavnRegistreringsVirkningEntity createVirkningEntity() {
                return new ReserveretVejnavnRegistreringsVirkningEntity(this);
        }
}
