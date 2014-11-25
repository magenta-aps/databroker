package dk.magenta.databroker.cprvejregister.model.reserveretvejnavn;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "reserveret_vejnavn_registrering")
public class ReserveretVejnavnVersionEntity
        extends DobbeltHistorikVersion<ReserveretVejnavnEntity, ReserveretVejnavnVersionEntity> {

        @ManyToOne(fetch = FetchType.LAZY)
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



        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "navneomraade", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
        private String navneomraade;

        @Basic
        @Column(name = "reserveret_navn", nullable = true, insertable = true, updatable = true, length = 255)
        private String reserveretNavn;

        @Basic
        @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
        private String status;

        @Basic
        @Column(name = "reservationsudloebsdato", nullable = true, insertable = true, updatable = true)
        private Date reservationsudloebsdato;

        @Basic
        @Column(name = "retskrivningskontrol", nullable = true, insertable = true, updatable = true, length = 255)
        private String retskrivningskontrol;


        public String getNavneomraade() {
                return navneomraade;
        }

        public void setNavneomraade(String navneomraade) {
                this.navneomraade = navneomraade;
        }

        public String getReserveretNavn() {
                return reserveretNavn;
        }

        public void setReserveretNavn(String reserveretNavn) {
                this.reserveretNavn = reserveretNavn;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public Date getReservationsudloebsdato() {
                return this.reservationsudloebsdato;
        }

        public void setReservationsudloebsdato(Date reservationsudloebsdato) {
                this.reservationsudloebsdato = reservationsudloebsdato;
        }

        public String getRetskrivningskontrol() {
                return this.retskrivningskontrol;
        }

        public void setRetskrivningskontrol(String retskrivningskontrol) {
                this.retskrivningskontrol = retskrivningskontrol;
        }
}
