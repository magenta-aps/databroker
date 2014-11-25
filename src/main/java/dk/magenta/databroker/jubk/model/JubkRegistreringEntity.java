package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk_registrering")
public class JubkRegistreringEntity  extends DobbeltHistorikVersion<JubkEntity, JubkRegistreringEntity> {

        /*
         * Versioning fields
         */

        @ManyToOne(optional = false)
        private JubkEntity entity;

        protected JubkRegistreringEntity() {
                super();
        }

        public JubkRegistreringEntity(JubkEntity entitet) {
                super(entitet);
        }

        @Override
        public JubkEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(JubkEntity entitet) {
                this.entity = entitet;
        }

        /*
         * Fields belonging to the version
         */

        @Basic
        @Column
        private String customData;

        public String getCustomData() {
                return customData;
        }

        public void setCustomData(String customData) {
                this.customData = customData;
        }
}
