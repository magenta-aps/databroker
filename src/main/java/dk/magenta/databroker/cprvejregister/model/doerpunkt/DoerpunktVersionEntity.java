package dk.magenta.databroker.cprvejregister.model.doerpunkt;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "doerpunkt_registrering")
public class DoerpunktVersionEntity
        extends DobbeltHistorikVersion<DoerpunktEntity, DoerpunktVersionEntity> {

        @Basic
        @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
        private String noejagtighedsklasse;

        @Basic
        @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
        private String kilde;

        @Basic
        @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
        private String tekniskStandard;


        @ManyToOne
        private DoerpunktEntity entity;

        public DoerpunktVersionEntity() {
                super();
        }

        public DoerpunktVersionEntity(DoerpunktEntity entity) {
                super(entity);
        }

        @Override
        public DoerpunktEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(DoerpunktEntity entitet) {
                this.entity = entitet;
        }
}
