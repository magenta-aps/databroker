package dk.magenta.databroker.cprvejregister.model.adgangspunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adgangspunkt_registrering")
public class AdgangspunktVersionEntity
        extends DobbeltHistorikVersion<AdgangspunktEntity, AdgangspunktVersionEntity> {

        @ManyToOne
        private AdgangspunktEntity entity;

        protected AdgangspunktVersionEntity() {
                super();
        }

        public AdgangspunktVersionEntity(AdgangspunktEntity entity) {
                super(entity);
        }

        @Override
        public AdgangspunktEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(AdgangspunktEntity entitet) {
                this.entity = entitet;
        }


        /********************************************************************************
         * Versioned data                                                               *
         ********************************************************************************/

        @Basic
        @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 255)
        private String status;

        @Basic
        @Column(name = "noejagtighedsklasse", nullable = false, insertable = true, updatable = true, length = 255)
        private String noejagtighedsklasse;

        @Basic
        @Column(name = "kilde", nullable = false, insertable = true, updatable = true, length = 255)
        private String kilde;

        @Basic
        @Column(name = "teknisk_standard", nullable = false, insertable = true, updatable = true, length = 255)
        private String tekniskStandard;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ligger_i_postnummer_id", nullable = false)
        private PostnummerEntity liggerIPostnummer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "position_id", nullable = false)
        private IsoPunktEntity position;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "adgangspunktsretning_id", nullable = false)
        private IsoPunktEntity adgangspunktsretning;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "husnummerretning_id", nullable = false)
        private IsoPunktEntity husnummerretning;


}
