package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class JubkRegistreringEntity extends DobbeltHistorikRegistrering<
        JubkEntity,
        JubkRegistreringEntity,
        JubkRegistreringsvirkningEntity
        > {

        @Column
        public String getCustom() {
                return custom;
        }

        public void setCustom(String custom) {
                this.custom = custom;
        }

        private String custom;

        public JubkRegistreringEntity() {
        }

        public JubkRegistreringEntity(
                JubkEntity entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger
        ) {
                super(entitet, registrering, virkninger);
        }
}
