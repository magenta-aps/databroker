package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVirkning;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */

@Entity
@Table(name = "postnummer_registreringsvirkning")
public class PostnummerRegistreringsVirkningEntity extends DobbeltHistorikVirkning<
        PostnummerEntity,
        PostnummerVersionEntity,
        PostnummerRegistreringsVirkningEntity> {

        public PostnummerRegistreringsVirkningEntity(PostnummerVersionEntity entitetsRegistrering) {
                super(entitetsRegistrering);
        }
}
