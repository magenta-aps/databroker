package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "kommune_registrering")
public class CprKommuneVersionEntity
        extends DobbeltHistorikVersion<CprKommuneEntity, CprKommuneVersionEntity> {

        @ManyToOne(fetch = FetchType.EAGER)
        private CprKommuneEntity entity;

        protected CprKommuneVersionEntity() {
                super();
        }

        public CprKommuneVersionEntity(CprKommuneEntity entity) {
                super(entity);
        }

        @Override
        public CprKommuneEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(CprKommuneEntity entitet) {
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