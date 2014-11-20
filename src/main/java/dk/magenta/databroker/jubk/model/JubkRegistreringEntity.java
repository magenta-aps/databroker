package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class JubkRegistreringEntity extends DobbeltHistorikRegistreringEntity<
        JubkEntity,
        JubkRegistreringEntity,
        JubkRegistreringsvirkningEntity
        > {

        public JubkRegistreringEntity() {
        }

        public JubkRegistreringEntity(
                JubkEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger
        ) {
                super(entitet, registrering, virkninger);
        }
}
