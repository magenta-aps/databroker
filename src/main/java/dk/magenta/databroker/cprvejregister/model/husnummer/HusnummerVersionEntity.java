package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "husnummer_registrering")
public class HusnummerVersionEntity
        extends DobbeltHistorikVersion<HusnummerEntity, HusnummerVersionEntity, HusnummerRegistreringsVirkningEntity> {

        public HusnummerVersionEntity(HusnummerEntity entitet) {
                super(entitet);
        }

        @Override
        protected HusnummerRegistreringsVirkningEntity createVirkningEntity() {
                return new HusnummerRegistreringsVirkningEntity(this);
        }
}
