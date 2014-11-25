package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "kommune_registrering")
public class KommuneVersionEntity
        extends DobbeltHistorikVersion<KommuneEntity, KommuneVersionEntity> {

        @ManyToOne(fetch = FetchType.LAZY)
        private KommuneEntity entity;

        protected KommuneVersionEntity() {
                super();
        }

        public KommuneVersionEntity(KommuneEntity entity) {
                super(entity);
        }

        @Override
        public KommuneEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(KommuneEntity entitet) {
                this.entity = entitet;
        }


        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "navn", nullable = false, insertable = true, updatable = true, length = 255)
        private String navn;

        public String getNavn() {
                return this.navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }

}
