package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "postnummer_registrering")
public class PostnummerVersionEntity
        extends DobbeltHistorikVersion<PostnummerEntity, PostnummerVersionEntity, PostnummerRegistreringsVirkningEntity> {

        public PostnummerVersionEntity(PostnummerEntity entitet) {
                super(entitet);
        }

        @Override
        protected PostnummerRegistreringsVirkningEntity createVirkningEntity() {
                return new PostnummerRegistreringsVirkningEntity(this);
        }
}
