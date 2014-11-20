package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk")
public class JubkEntity extends DobbeltHistorikEntity<
        JubkEntity,
        JubkRegistreringEntity,
        JubkRegistreringsvirkningEntity
        > {
        public JubkEntity() {
                super();
        }

        public JubkEntity(String uuid, String brugervendtNoegle) {
                super(uuid, brugervendtNoegle);
        }
}
