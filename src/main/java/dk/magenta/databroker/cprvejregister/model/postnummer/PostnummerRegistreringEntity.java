package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "postnummer_registrering")
public class PostnummerRegistreringEntity
        extends DobbeltHistorikRegistrering<PostnummerEntity, PostnummerRegistreringEntity, PostnummerRegistreringsVirkningEntity> {

        public PostnummerRegistreringEntity(PostnummerEntity entitet) {
                super(entitet);
        }

        @Override
        protected PostnummerRegistreringsVirkningEntity createVirkningEntity() {
                return new PostnummerRegistreringsVirkningEntity(this);
        }
}
