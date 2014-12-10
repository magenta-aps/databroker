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
        @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
        private String status;

        @Basic
        @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
        private String noejagtighedsklasse;

        @Basic
        @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
        private String kilde;

        @Basic
        @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
        private String tekniskStandard;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ligger_i_postnummer_id", nullable = true)
        private PostnummerEntity liggerIPostnummer;

        public PostnummerEntity getLiggerIPostnummer() {
                return liggerIPostnummer;
        }

        public void setLiggerIPostnummer(PostnummerEntity liggerIPostnummer) {
                this.liggerIPostnummer = liggerIPostnummer;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "position_id", nullable = true)
        private IsoPunktEntity position;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "adgangspunktsretning_id", nullable = true)
        private IsoPunktEntity adgangspunktsretning;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "husnummerretning_id", nullable = true)
        private IsoPunktEntity husnummerretning;


}
