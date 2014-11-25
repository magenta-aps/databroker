package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "husnummer_registrering")
public class HusnummerVersionEntity
        extends DobbeltHistorikVersion<HusnummerEntity, HusnummerVersionEntity> {


        @ManyToOne
        private HusnummerEntity entity;

        protected HusnummerVersionEntity() {
                super();
        }

        public HusnummerVersionEntity(HusnummerEntity entity) {
                super(entity);
        }



        @Override
        public HusnummerEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(HusnummerEntity entitet) {
                this.entity = entitet;
        }

}
