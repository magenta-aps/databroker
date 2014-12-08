package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adresse_registrering")
public class AdresseVersionEntity
        extends DobbeltHistorikVersion<AdresseEntity, AdresseVersionEntity> {

        @ManyToOne
        private AdresseEntity entity;

        public AdresseVersionEntity() {
                super();
        }

        public AdresseVersionEntity(AdresseEntity entity) {
                this.entity = entity;
        }

        @Override
        public AdresseEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(AdresseEntity entitet) {
                this.entity = entitet;
        }

        /********************************************************************************
         * Versioned data                                                               *
         ********************************************************************************/

        @Basic
        @Column
        private String status;

        @Basic
        @Column
        @Index(name = "etageBetegnelse")
        private String etageBetegnelse;

        @Basic
        @Column
        @Index(name = "doerBetegnelse")
        private String doerBetegnelse;

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getEtageBetegnelse() {
                return etageBetegnelse;
        }

        public void setEtageBetegnelse(String etageBetegnelse) {
                this.etageBetegnelse = etageBetegnelse;
        }

        public String getDoerBetegnelse() {
                return doerBetegnelse;
        }

        public void setDoerBetegnelse(String doerBetegnelse) {
                this.doerBetegnelse = doerBetegnelse;
        }
}
