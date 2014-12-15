package dk.magenta.databroker.cprvejregister.model.lokalitet;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by lars on 12/12/14.
 */
@Entity
@Table(name = "lokalitet_registrering")
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class LokalitetVersionEntity
    extends DobbeltHistorikVersion<LokalitetEntity, LokalitetVersionEntity> {


        @ManyToOne(fetch = FetchType.EAGER)
        private LokalitetEntity entity;

        protected LokalitetVersionEntity() {
            super();
        }

        public LokalitetVersionEntity(LokalitetEntity entity) {
            super(entity);
        }

        @Override
        public LokalitetEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(LokalitetEntity entitet) {
                this.entity = entitet;
        }


        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "lokalitetsnavn", nullable = false, insertable = true, updatable = true)
        private String lokalitetsNavn;

        public String getLokalitetsNavn() {
            return this.lokalitetsNavn;
        }

        public void setLokalitetsNavn(String lokalitetsNavn) {
            this.lokalitetsNavn = lokalitetsNavn;
        }


}
