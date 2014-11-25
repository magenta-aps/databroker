package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

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
        private String etageBetegnelse;

        @Basic
        @Column
        private String doerBetegnelse;
}
