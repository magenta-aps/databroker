package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.OioRegistreringBase;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class JubkRegistreringEntity  extends OioRegistreringBase<JubkEntity, JubkRegistreringEntity> {
        @ManyToOne
        private JubkEntity entitet;

        @Override
        public JubkEntity getEntitet() {
                return entitet;
        }

        private void setEntitet(JubkEntity entitet) {
                this.entitet = entitet;
        }
}
