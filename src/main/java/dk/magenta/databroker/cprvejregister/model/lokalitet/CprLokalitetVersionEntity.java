package dk.magenta.databroker.cprvejregister.model.lokalitet;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by lars on 12/12/14.
 */
@Entity
@Table(name = "lokalitet_registrering")
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class CprLokalitetVersionEntity
    extends DobbeltHistorikVersion<CprLokalitetEntity, CprLokalitetVersionEntity> {


        @ManyToOne(fetch = FetchType.EAGER)
        private CprLokalitetEntity entity;

        protected CprLokalitetVersionEntity() {
            super();
        }

        public CprLokalitetVersionEntity(CprLokalitetEntity entity) {
            super(entity);
        }

        @Override
        public CprLokalitetEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(CprLokalitetEntity entitet) {
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
