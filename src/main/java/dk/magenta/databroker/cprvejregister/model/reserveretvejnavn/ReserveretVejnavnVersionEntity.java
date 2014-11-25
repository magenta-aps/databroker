package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "reserveret_vejnavn_registrering")
public class ReserveretVejnavnVersionEntity
        extends DobbeltHistorikVersion<ReserveretVejnavnEntity, ReserveretVejnavnVersionEntity> {

        @ManyToOne
        private ReserveretVejnavnEntity entity;

        protected ReserveretVejnavnVersionEntity() {
                super();
        }

        public ReserveretVejnavnVersionEntity(ReserveretVejnavnEntity entity) {
                super(entity);
        }

        @Override
        public ReserveretVejnavnEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(ReserveretVejnavnEntity entitet) {
                this.entity = entitet;
        }
}
