package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "postnummer_registrering")
public class PostnummerVersionEntity
        extends DobbeltHistorikVersion<PostnummerEntity, PostnummerVersionEntity> {

        @ManyToOne
        private PostnummerEntity entity;

        protected PostnummerVersionEntity() {
                super();
        }

        public PostnummerVersionEntity(PostnummerEntity entity) {
                super(entity);
        }

        @Override
        public PostnummerEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(PostnummerEntity entitet) {
                this.entity = entitet;
        }
}
