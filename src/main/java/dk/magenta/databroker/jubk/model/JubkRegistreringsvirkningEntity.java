package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringsvirkningEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "jubk_jubk_registreringsvirkning")
public class JubkRegistreringsvirkningEntity extends DobbeltHistorikRegistreringsvirkningEntity<
        JubkEntity,
        JubkRegistreringEntity,
        JubkRegistreringsvirkningEntity> {

        public JubkRegistreringsvirkningEntity() {
        }

        public JubkRegistreringsvirkningEntity(JubkRegistreringEntity entitetsRegistrering, VirkningEntity virkning) {
                super(entitetsRegistrering, virkning);
        }
}